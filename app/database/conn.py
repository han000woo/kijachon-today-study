from fastapi import FastAPI
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
import logging
from app.database.base import Base

class SQLAlchemy:
    def __init__(self, app: FastAPI = None, **kwargs):
        self._engine = None
        self._session = None
        if app is not None:
            self.init_app(app=app, **kwargs)

    def init_app(self, app: FastAPI, **kwargs):
        """
        DB 초기화 함수
        :param app: FastAPI 인스턴스
        :param kwargs: DB_URL, DB_POOL_RECYCLE, DB_ECHO 등
        """
        database_url = kwargs.get("DB_URL")
        pool_recycle = kwargs.get("DB_POOL_RECYCLE", 900)
        echo = kwargs.get("DB_ECHO", True)

        self._engine = create_engine(
            database_url,
            echo=echo,
            pool_recycle=pool_recycle,
            pool_pre_ping=True,
        )
        self._session = sessionmaker(autocommit=False, autoflush=False, bind=self._engine)

        @app.on_event("startup")
        def startup():
            # 커넥션 풀 초기화 확인용으로 connect() 호출 가능
            with self._engine.connect() as conn:
                logging.info("DB connected")

        @app.on_event("shutdown")
        def shutdown():
            from sqlalchemy.orm import close_all_sessions
            close_all_sessions()
            self._engine.dispose()
            logging.info("DB disconnected")

    def create_tables(self):
        """
        DB에 정의된 모든 테이블 생성
        """
        Base.metadata.create_all(bind=self._engine)
        logging.info("Tables created")

    def get_db(self):
        """
        DB 세션 생성 및 반환, 요청마다 호출되는 의존성 함수
        """
        if self._session is None:
            raise RuntimeError("init_app must be called before using get_db")

        db_session = self._session()
        try:
            yield db_session
        finally:
            db_session.close()

    @property
    def session(self):
        """
        FastAPI 의존성 주입용 프로퍼티 (yield 함수)
        사용 예: Depends(db.session)
        """
        return self.get_db

    @property
    def engine(self):
        return self._engine

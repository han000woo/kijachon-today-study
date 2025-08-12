from dataclasses import asdict
from fastapi import FastAPI, Depends
import uvicorn as uvicorn
from fastapi.middleware.cors import CORSMiddleware

from database.conn import SQLAlchemy
from app.common.config import conf
from app.routes import index
from fastapi.staticfiles import StaticFiles
from common.config import Config
def create_app():
    """
    앱 함수 실행
    :return:
    """
    c = conf()
    app = FastAPI()
    conf_dict = asdict(c)
    db = SQLAlchemy()
    db.init_app(app, **conf_dict)
    db.create_tables()

    app.state.db = db

    # CORS 설정 추가
    app.add_middleware(
        CORSMiddleware,
        allow_origins=["*"],  # 프론트엔드 주소
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )


    # 라우터 정의
    app.include_router(index.router)
    # app.include_router(auth.router, tags=["Authentication"], prefix="/auth")
    # app.include_router(users.router, tags=["Users"], prefix="/api", dependencies=[Depends(API_KEY_HEADER)])

    # css 마운트
    app.mount("/static", StaticFiles(directory="static"), name="static")

    return app


app = create_app()

# 실행 파일을 여기로 했을 경우
if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=Config.SERVER_PORT, reload=True)
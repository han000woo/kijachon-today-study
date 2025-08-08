from dataclasses import asdict
from fastapi import FastAPI, Depends
import uvicorn as uvicorn

from backend.app.common.config import conf
from database.conn import db
from routes import index
def create_app():
    """
    앱 함수 실행
    :return:
    """
    c = conf()
    app = FastAPI()
    conf_dict = asdict(c)
    db.init_app(app, **conf_dict)
    db.create_tables()
    # 데이터 베이스 이니셜라이즈


    # 라우터 정의
    app.include_router(index.router)
    # app.include_router(auth.router, tags=["Authentication"], prefix="/auth")
    # app.include_router(users.router, tags=["Users"], prefix="/api", dependencies=[Depends(API_KEY_HEADER)])
    return app


app = create_app()

# 실행 파일을 여기로 했을 경우
if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8080, reload=True)
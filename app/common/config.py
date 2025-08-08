from dataclasses import dataclass, asdict
from os import path, environ

# 상대경로를 하기 어렵기 때문에 절대 경로를 위해서 관리 용도로 사용한다.
base_dir = path.dirname(path.dirname(path.dirname(path.abspath(__file__))))

@dataclass
class Config:
    """
    기본 Configuration
    """
    BASE_DIR = base_dir

    DB_POOL_RECYCLE: int = 900
    DB_ECHO: bool = True


@dataclass
class LocalConfig(Config):  # Config를 상속받음
    # MySQL 관련 변수
    # DB_USERNAME: str = "travis"
    # DB_PASSWORD: str = "Qjqan12#"
    # DB_HOST: str = "localhost"
    # DB_PORT: str = "3306"
    # DB_NAME: str = "notification_api"

    # SQLite는 파일 기반이므로, DB_URL만 설정
    DB_FILE_NAME: str = "study.db"
    DB_URL: str = f"sqlite:///{path.join(base_dir, DB_FILE_NAME)}"
    TRUSTED_HOSTS = ["*"]
    ALLOW_SITE = ["*"]


@dataclass
class ProdConfig(Config):
    TRUSTED_HOSTS = ["*"]
    ALLOW_SITE = ["*"]


def conf():
    """
    환경 불러오기
    :return:
    """
    config = dict(prod=ProdConfig(), local=LocalConfig())
    return config.get(environ.get("API_ENV", "local"))
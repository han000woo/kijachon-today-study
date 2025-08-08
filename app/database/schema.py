from datetime import datetime, timedelta
from enum import Enum as PyEnum  # Python Enum에 별칭(alias) 지정
from sqlalchemy import (
    Column,
    Integer,
    String,
    DateTime,
    Text,
    func,
    Enum,  # SQLAlchemy의 Enum 타입
    Boolean,
)
from .base import Base


class DifficultyLevel(PyEnum):  # 이제 PyEnum을 상속받아 정상적으로 동작
    EASY = 1
    NORMAL = 2
    HARD = 3


class UnderstandingLevel(PyEnum):  # 마찬가지로 PyEnum을 상속
    LOW = 1
    MEDIUM = 2
    HIGH = 3


class StudyLog(Base):
    __tablename__ = 'study_log'
    id = Column(Integer, primary_key=True, autoincrement=True)
    start_time = Column(DateTime, nullable=False)
    end_time = Column(DateTime, nullable=False)
    # 여기서는 여전히 SQLAlchemy의 Enum 타입을 사용합니다.
    difficulty_level = Column(Enum(DifficultyLevel), nullable=False)
    understanding_level = Column(Enum(UnderstandingLevel), nullable=False)
    contents = Column(Text, nullable=False)
    memo = Column(Text)
    reference = Column(String(255))

    def __repr__(self):
        return f"<" \
               f"StudyLog(" \
               f"id='{self.id}', " \
               f"contents='{self.contents[:20]}'," \
               f"contents='{self.memo[:20]}'," \
               f"contents='{self.difficulty_level}'," \
               f"contents='{self.understanding_level}'," \
               f"contents='{self.reference[:20]}'," \
               f"start_time='{self.start_time}'," \
               f"end_time='{self.end_time}')>"

class User(Base):
    __tablename__ = 'user'
    id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String(50), nullable=False)
    age = Column(Integer, nullable=False)
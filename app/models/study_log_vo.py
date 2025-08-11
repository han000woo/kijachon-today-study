from datetime import datetime
from typing import Optional
from pydantic import BaseModel
from ..database.schema import StudyLog
from ..database.schema import DifficultyLevel, UnderstandingLevel


class StudyLogVO(BaseModel):
    """
    공부 기록 폼에서 제출된 데이터를 담는 VO (Value Object)
    """
    subject: str
    start_time: datetime
    end_time: datetime
    category: str
    difficulty: int
    understanding: int
    contents: str
    memo: Optional[str] = None
    reference: Optional[str] = None

    def __repr__(self):
        return (f"StudyLog(subject='{self.subject}', "
                f"start_time='{self.start_time}', "
                f"end_time='{self.end_time}', "
                f"category='{self.category}', "
                f"difficulty={self.difficulty}, "
                f"understanding={self.understanding})")

    def convert_to_schema(self):
        """
        VO 객체를 SQLAlchemy 스키마로 변환하는 메서드.
        변환 로직을 별도 메서드로 분리하여 사용합니다.
        """
        return StudyLog(
            subject=self.subject,
            category=self.category,
            start_time=self.start_time,
            end_time=self.end_time,
            difficulty_level=self._get_difficulty_level(),
            understanding_level=self._get_understanding_level(),
            contents=self.contents,
            memo=self.memo,
            reference=self.reference
        )

    def _get_difficulty_level(self):
        """정수형 난이도를 DifficultyLevel Enum으로 변환합니다."""
        if self.difficulty == 1:
            return DifficultyLevel.EASY
        elif self.difficulty == 3:
            return DifficultyLevel.HARD
        return DifficultyLevel.NORMAL

    def _get_understanding_level(self):
        """정수형 이해도를 UnderstandingLevel Enum으로 변환합니다."""
        if self.understanding == 1:
            return UnderstandingLevel.LOW
        elif self.understanding == 3:
            return UnderstandingLevel.HIGH
        elif self.understanding == 4:
            return UnderstandingLevel.PERFECT
        return UnderstandingLevel.MEDIUM
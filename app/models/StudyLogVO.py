from datetime import time
from typing import Optional


class StudyLog:
    """
    공부 기록 폼에서 제출된 데이터를 담는 VO (Value Object)
    """

    def __init__(self,
                 subject: str,
                 start_time: time,
                 end_time: time,
                 category: str,
                 difficulty: int,
                 understanding: int,
                 content: str,
                 memo: Optional[str] = None,
                 reference: Optional[str] = None):
        self.subject = subject
        self.start_time = start_time
        self.end_time = end_time
        self.category = category
        self.difficulty = difficulty
        self.understanding = understanding
        self.content = content
        self.memo = memo
        self.reference = reference

    def __repr__(self):
        return (f"StudyLog(subject='{self.subject}', "
                f"start_time='{self.start_time}', "
                f"end_time='{self.end_time}', "
                f"category='{self.category}', "
                f"difficulty={self.difficulty}, "
                f"understanding={self.understanding})")
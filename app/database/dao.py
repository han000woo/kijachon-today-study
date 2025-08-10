# dao/study_log_dao.py
# 데이터베이스와 직접 통신하는 DAO(Data Access Object) 계층

from sqlalchemy.orm import Session
from ..database.schema import StudyLog


class study_dao :
    def add_study_log(
            session: Session,
            study_log: StudyLog
    ):
        """
        StudyLog 객체를 DB에 추가하고 커밋합니다.
        """
        session.add(study_log)
        session.commit()
        session.refresh(study_log)
        return study_log

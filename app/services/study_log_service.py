# services/study_log_service.py
# 비즈니스 로직을 처리하는 서비스 계층
import logging
from ..database.dao import study_dao
from ..models.study_log_vo import StudyLogVO
from fastapi.responses import HTMLResponse
from sqlalchemy.orm import Session


def create_study_log_entry(
        session : Session,
        study_log_vo: StudyLogVO
):
    """
    VO 객체를 DAO로 전달하기 전에 유효성 검사 및 변환을 수행합니다.
    """
    try:
        logging.info("세션 가져오기")
        # VO 객체에 정의된 메서드를 사용하여 SQLAlchemy 모델로 변환
        study_log = study_log_vo.convert_to_schema()
        logging.info("vo -> schema")
        # DAO를 호출하여 DB에 저장
        study_dao.add_study_log(session=session, study_log=study_log)
        logging.info("DB 저장")
        logging.info(f"새로운 공부 기록이 등록되었습니다: {study_log_vo.subject}")

        return HTMLResponse(content="<div class='success-message'>✅ 공부 기록이 성공적으로 저장되었습니다!</div>", status_code=200)
    except Exception as e:
        # 오류 발생 시 세션 롤백
        session.rollback()
        logging.error(f"공부 기록 저장 중 오류 발생: {e}")
        # 오류 발생 시 사용자에게 보여줄 HTML 응답
        return HTMLResponse(
            content=f"<div class='error-message'>⚠️ 오류가 발생했습니다: {e}</div>",
            status_code=500
        )

# services/study_log_service.py
# 비즈니스 로직을 처리하는 서비스 계층
import logging

from starlette.responses import JSONResponse
from ..database.dao import study_dao
from ..models.study_log_vo import StudyLogVO
from sqlalchemy.orm import Session
import random

messages = [
    "오늘도 훌륭해요! 꾸준함이 정말 멋져요 😊",
    "멋져요! 오늘도 한 걸음 나아갔네요 👏",
    "수고 많았어요. 당신의 노력이 빛나고 있어요 ✨",
    "공부왕 등장! 오늘도 레벨업 성공 🎉",
    "기록 완료! 성실함이 돋보여요 😊"
]

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

        # 성공 응답
        return JSONResponse(content={
            "status": "success",
            "message" : random.choice(messages)
        }, status_code=200)

    except Exception as e:
        # 오류 발생 시 세션 롤백
        session.rollback()
        logging.error(f"공부 기록 저장 중 오류 발생: {e}")
        # 실패 응답
        return JSONResponse(content={
            "status": "error",
            "message": "시스템 에러"
        }, status_code=500)

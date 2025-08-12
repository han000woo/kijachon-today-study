import logging
from typing import Optional, Union
from fastapi import APIRouter, Request, Form, Depends
from sqlalchemy.orm import Session
from datetime import datetime, timedelta

from starlette.responses import JSONResponse

from app.models.study_log_vo import StudyLogVO
from app.services.study_log_service import create_study_log_entry

router = APIRouter()


def get_db(request: Request) -> Session:
    """FastAPI 앱에서 DB 세션을 가져오는 의존성 함수"""
    db = request.app.state.db
    yield from db.get_db()

def resolve_study_times(start_time_str, end_time_str):
    """
    start_time_str, end_time_str: e.g. "11:00 PM", "02:00 AM"
    Returns: start_dt, end_dt (datetime objects)
    """

    # 시간 문자열을 datetime.time 객체로 파싱
    start_time = datetime.strptime(start_time_str, "%I:%M %p").time()
    end_time = datetime.strptime(end_time_str, "%I:%M %p").time()

    # 오늘 날짜 기준
    today = datetime.now().date()
    yesterday = today - timedelta(days=1)

    # 시작 시간이 종료 시간보다 늦으면 → 날짜가 넘어간 것으로 간주
    if start_time > end_time:
        start_dt = datetime.combine(yesterday, start_time)
        end_dt = datetime.combine(today, end_time)
    else:
        start_dt = datetime.combine(today, start_time)
        end_dt = datetime.combine(today, end_time)

    return start_dt, end_dt

def get_study_log_vo(
    subject: str = Form(...),
    start_time: str = Form(...),
    end_time: str = Form(...),
    category: str = Form(...),
    difficulty: int = Form(...),
    understanding: int = Form(...),
    contents: str = Form(...),
    memo: Optional[str] = Form(None),
    reference: Optional[str] = Form(None)
) -> StudyLogVO:
    """폼 데이터를 StudyLogVO 객체로 변환하는 의존성 함수"""

    try:
        # ISO 8601 형식: 2025-08-11T15:49
        start_dt, end_dt = resolve_study_times(start_time, end_time)

    except ValueError as e:
        logging.error(f"time parsing error: {e}")
        raise

    vo = StudyLogVO(
        subject=subject,
        start_time=start_dt,
        end_time=end_dt,
        category=category,
        difficulty=difficulty,
        understanding=understanding,
        contents=contents,
        memo=memo,
        reference=reference
    )

    return vo


@router.post("/submit-study", response_class=JSONResponse)
async def add_study_log(
    study_log_vo: StudyLogVO = Depends(get_study_log_vo),
    session: Session = Depends(get_db)
):
    logging.info("submit-study")
    """공부 기록 제출 처리"""
    response = create_study_log_entry(session, study_log_vo)
    return response


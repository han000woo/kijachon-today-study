import logging
from typing import Optional, Union
from fastapi import APIRouter, Request, Form, Depends
from fastapi.templating import Jinja2Templates
from sqlalchemy.orm import Session
from datetime import datetime

from app.models.study_log_vo import StudyLogVO
from app.services.study_log_service import create_study_log_entry

router = APIRouter()
templates = Jinja2Templates(directory="templates")


def get_db(request: Request) -> Session:
    """FastAPI 앱에서 DB 세션을 가져오는 의존성 함수"""
    db = request.app.state.db
    yield from db.get_db()


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
        start_dt = datetime.strptime(start_time, "%Y-%m-%dT%H:%M")
        end_dt = datetime.strptime(end_time, "%Y-%m-%dT%H:%M")

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


@router.get("/")
def read_form(request: Request):
    """공부 기록 폼 페이지 렌더링"""
    return templates.TemplateResponse("study_log_form.html", {"request": request})


@router.post("/submit-study")
async def add_study_log(
    study_log_vo: StudyLogVO = Depends(get_study_log_vo),
    session: Session = Depends(get_db)
):
    """공부 기록 제출 처리"""
    response = create_study_log_entry(session, study_log_vo)
    return response


@router.get("/items/{item_id}")
def read_item(item_id: int, q: Optional[str] = None):
    """예시용 라우터"""
    return {"item_id": item_id, "q": q}

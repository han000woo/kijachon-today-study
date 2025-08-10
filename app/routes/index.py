import logging
from typing import Union
from fastapi import APIRouter, Request, Form, Depends
from fastapi.templating import Jinja2Templates
from app.models.study_log_vo import StudyLogVO
from app.services.study_log_service import create_study_log_entry
from sqlalchemy.orm import Session
from database.conn import db

router = APIRouter()
templates = Jinja2Templates(directory="templates")

# 폼 데이터를 Pydantic 모델로 자동 변환하는 의존성
def get_study_log_vo(
    subject: str = Form(...),
    start_time: str = Form(...),
    end_time: str = Form(...),
    category: str = Form(...),
    difficulty: int = Form(...),
    understanding: int = Form(...),
    contents: str = Form(...),
    memo: str = Form(None),
    reference: str = Form(None)
):
    return StudyLogVO(
        subject=subject,
        start_time=start_time,
        end_time=end_time,
        category=category,
        difficulty=difficulty,
        understanding=understanding,
        contents=contents,
        memo=memo,
        reference=reference
    )

@router.get("/")
def read_root(request: Request):
    return templates.TemplateResponse("study_log_form.html", {"request": request})


@router.post("/submit-study")
async def add_study_log(
    study_log_vo: StudyLogVO = Depends(get_study_log_vo),
    session: Session = Depends(db.get_db)
):
    response = create_study_log_entry(session, study_log_vo)

    return response


@router.get("/items/{item_id}")
def read_item(item_id: int, q: Union[str, None] = None):
    return {"item_id": item_id, "q": q}



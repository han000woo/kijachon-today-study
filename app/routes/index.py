import logging
from typing import Union
from fastapi import APIRouter, Request, Form
from fastapi.responses import HTMLResponse
from fastapi.templating import Jinja2Templates
from app.models.StudyLogVO import StudyLog

router = APIRouter()
templates = Jinja2Templates(directory="templates")

@router.get("/")
def read_root(request: Request):
    return templates.TemplateResponse("study_log_form.html", {"request": request})


@router.post("/submit-study")
async def add_study_log(
        request: Request,
        subject: str = Form(...),
        start_time: str = Form(...),
        end_time: str = Form(...),
        category: str = Form(...),
        difficulty: int = Form(...),
        understanding: int = Form(...),
        content: str = Form(...),
        memo: str = Form(None),
        reference: str = Form(None)
):
    # 폼 데이터를 StudyLog VO 객체에 담기
    study_log = StudyLog(
        subject=subject,
        start_time=start_time,
        end_time=end_time,
        category=category,
        difficulty=difficulty,
        understanding=understanding,
        content=content,
        memo=memo,
        reference=reference
    )

    # 여기서 데이터베이스에 `study_log` 객체를 저장하는 로직을 추가합니다.
    # 예: db.save(study_log)
    logging.info(f"새로운 공부 기록이 등록되었습니다: {study_log.subject}")
    print(study_log)
    return HTMLResponse(content="<div class='success-message'>✅ 공부 기록이 성공적으로 저장되었습니다!</div>", status_code=200)


@router.get("/items/{item_id}")
def read_item(item_id: int, q: Union[str, None] = None):
    return {"item_id": item_id, "q": q}

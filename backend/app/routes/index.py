from typing import Union
from fastapi import APIRouter, Request
from fastapi.templating import Jinja2Templates

router = APIRouter()
templates = Jinja2Templates(directory="templates")

@router.get("/")
def read_root(request: Request):
    return templates.TemplateResponse("study_log_form.html", {"request": request})

@router.post("/submit-study")
def add_study_log(request: Request):
    return

@router.get("/items/{item_id}")
def read_item(item_id: int, q: Union[str, None] = None):
    return {"item_id": item_id, "q": q}

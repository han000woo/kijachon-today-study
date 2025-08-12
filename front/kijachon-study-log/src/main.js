import htmx from 'htmx.org'
import Toastify from 'toastify-js'
import "toastify-js/src/toastify.css"

//timePicker 관련 함수
import { TimepickerUI } from "timepicker-ui";
import "timepicker-ui/main.css";
import { get_now_time } from './js/timepicker';

window.htmx = htmx;
window.Toastify = Toastify;
window.TimepickerUI = TimepickerUI;
window.get_now_time = get_now_time;

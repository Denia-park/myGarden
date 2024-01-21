import axios from "axios";
import {
    isToday,
    saveLastStartDateTimeInLocalStorage,
    updateLastStartDateTime
} from "@/components/dailyRoutine/api/util.js";

export function getDailyRoutineApi(startDateTime, endDateTime) {
    return axios.get(`/api/daily-routine?startDateTime=${startDateTime}&endDateTime=${endDateTime}`)
        .then(({data}) => {
            const allDateTimeDataArray = data.data;

            //오늘 날짜인 경우에만 LocalStorage를 업데이트
            if (isToday(startDateTime)) {
                saveLastStartDateTimeInLocalStorage(startDateTime, allDateTimeDataArray);
            }

            return {
                allDateTimeDataArray
            };
        })
        .catch(error => {
            console.log(error)
            alert('일정을 불러오는데 실패했습니다.')
        });
}


export function postDailyRoutineApi(startDate, endDate, routineType, content) {
    axios.post('/api/daily-routine', {
        startDateTime: startDate,
        endDateTime: endDate,
        routineType: routineType,
        routineDescription: content
    })
        .then(() => {
            alert("등록되었습니다.");
            location.reload();
        })
        .catch(error => {
            alert("등록에 실패했습니다.");

        });
}

export function updateDailyRoutineApi(id, startDate, endDate, routineType, content) {
    axios.put(`/api/daily-routine/${id}`, {
        startDateTime: startDate,
        endDateTime: endDate,
        routineType: routineType,
        routineDescription: content
    })
        .then(() => {
            alert("수정되었습니다.");

            updateLastStartDateTime(endDate);

            location.reload();
        })
        .catch(error => {
            alert("수정에 실패했습니다.");

        });
}

export function deleteDailyRoutineApi(id) {
    axios.delete(`/api/daily-routine/${id}`)
        .then(() => {
            alert("삭제되었습니다.");
            location.reload();
        })
        .catch(error => {
            alert("삭제에 실패했습니다.");

        });
}

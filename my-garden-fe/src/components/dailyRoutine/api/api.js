import axios from "axios";

export function getDailyRoutineApi(startDateTime, endDateTime) {
    function saveLastStartDateTimeInLocalStorage(allDateTimeDataArray) {
        const saveStartDateTime = calculateTodayLastStartDateTime(startDateTime, allDateTimeDataArray);

        if (localStorage.getItem('todayLastStartDateTime') !== saveStartDateTime) {
            localStorage.setItem('todayLastStartDateTime', saveStartDateTime);
            location.reload();
        }
    }

    function calculateTodayLastStartDateTime(todayStartDateTime, allDateTimeData) {
        let returnValue = todayStartDateTime;

        if (allDateTimeData.length !== 0) {
            returnValue = allDateTimeData[allDateTimeData.length - 1].endDateTime;
        }

        return returnValue;
    }

    return axios.get(`/api/daily-routine?startDateTime=${startDateTime}&endDateTime=${endDateTime}`)
        .then(({data}) => {
            const allDateTimeDataArray = data.data;

            //오늘 날짜인 경우에만 LocalStorage를 업데이트
            if (isToday(startDateTime)) {
                saveLastStartDateTimeInLocalStorage(allDateTimeDataArray);
            }

            return {
                allDateTimeDataArray
            };
        })
        .catch(error => {
            alert('일정을 불러오는데 실패했습니다.')
        });
}

function isToday(targetDate) {
    return getTodayDate() === targetDate.split('T')[0];
}

export function getTargetDateTimeRange(targetDate) {
    const targetStartDateTime = `${targetDate}T00:00:00`;
    const targetEndDateTime = `${targetDate}T23:59:59`;

    return {targetStartDateTime, targetEndDateTime};
}

export function getTodayDate() {
    const currentDate = new Date();

    const year = currentDate.getFullYear();
    const month = String(currentDate.getMonth() + 1).padStart(2, '0');
    const day = String(currentDate.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
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
    function updateLastStartDateTime(endDate) {
        const todayLastStartDateTime = localStorage.getItem("todayLastStartDateTime");

        if (todayLastStartDateTime < endDate) {
            localStorage.setItem("todayLastStartDateTime", endDate);
        }
    }

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

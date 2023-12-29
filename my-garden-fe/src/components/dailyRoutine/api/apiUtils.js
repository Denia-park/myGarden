import axios from "axios";

export async function getDailyRoutineApi(startDateTime, endDateTime) {
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
            saveLastStartDateTimeInLocalStorage(allDateTimeDataArray);

            return {
                allDateTimeDataArray
            };
        })
        .catch(error => {
            alert('일정을 불러오는데 실패했습니다.')
            console.log(error);
        });
}

export function getTodayDateTimeRange() {
    const currentDate = new Date();

    const year = currentDate.getFullYear();
    const month = String(currentDate.getMonth() + 1).padStart(2, '0');
    const day = String(currentDate.getDate()).padStart(2, '0');

    const todayStartDateTime = `${year}-${month}-${day}T00:00:00`;
    const todayEndDateTime = `${year}-${month}-${day}T23:59:59`;

    return {todayStartDateTime, todayEndDateTime};
}

export function getTodayDate() {
    const currentDate = new Date();

    const year = currentDate.getFullYear();
    const month = String(currentDate.getMonth() + 1).padStart(2, '0');
    const day = String(currentDate.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
}

export async function postDailyRoutineApi(startDate, endDate, routineType, content) {
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
            console.log(error);
        });
}

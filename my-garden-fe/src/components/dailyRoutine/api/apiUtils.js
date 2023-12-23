import axios from "axios";

export async function fetchTodayDailyRoutine() {
    const {todayStartDateTime, todayEndDateTime} = getTodayDateTimeRange();

    return axios.get(`/api/daily-routine?startDateTime=${todayStartDateTime}&endDateTime=${todayEndDateTime}`)
        .then(({data}) => {
            const allDateTimeDataArray = data.data;
            const todayFirstDateTime = calculateTodayFirstDateTime(todayStartDateTime, allDateTimeDataArray);

            return {
                allDateTimeDataArray,
                todayFirstDateTime
            };
        })
        .catch(error => {
            alert('오늘의 일정을 불러오는데 실패했습니다.')
            console.log(error);
        });
}

function getTodayDateTimeRange() {
    const currentDate = new Date();

    const year = currentDate.getFullYear();
    const month = String(currentDate.getMonth() + 1).padStart(2, '0');
    const day = String(currentDate.getDate()).padStart(2, '0');

    const todayStartDateTime = `${year}-${month}-${day}T00:00:00`;
    const todayEndDateTime = `${year}-${month}-${day}T23:59:59`;

    return {todayStartDateTime, todayEndDateTime};
}

function calculateTodayFirstDateTime(todayStartDateTime, allDateTimeData) {
    let returnValue = todayStartDateTime;

    if (allDateTimeData.length !== 0) {
        returnValue = allDateTimeData[allDateTimeData.length - 1].endDateTime;
    }

    return returnValue;
}

export async function postDailyRoutine(startDate, endDate, routineType, content) {
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
            alert("등록에 실패하였습니다.");
            console.log(error);
        });
}

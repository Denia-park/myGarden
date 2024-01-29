export function getTargetDateTimeRange(targetDate) {
    const targetStartDateTime = `${targetDate}T00:00:00`;
    const targetEndDateTime = `${targetDate}T23:59:59`;

    return {targetStartDateTime, targetEndDateTime};
}

export function isToday(targetDate) {
    return getTodayDate() === targetDate.split('T')[0];
}

export function getTodayDate() {
    const currentDate = new Date();

    const year = currentDate.getFullYear();
    const month = String(currentDate.getMonth() + 1).padStart(2, '0');
    const day = String(currentDate.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
}

export function getOneMonthAgoDate() {
    const currentDate = new Date();

    currentDate.setMonth(currentDate.getMonth() - 1);

    const year = currentDate.getFullYear();
    const month = String(currentDate.getMonth() + 1).padStart(2, '0');
    const day = String(currentDate.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
}

export function timeToMinutes(time) {
    const [hours, minutes] = time.split(':').map(Number);
    return hours * 60 + minutes;
}

export function extractTime(dateTime) {
    return dateTime.split('T')[1].slice(0, 5);
}

export function updateLastStartDateTime(endDate) {
    const todayLastStartDateTime = localStorage.getItem("todayLastStartDateTime");

    if (todayLastStartDateTime < endDate) {
        localStorage.setItem("todayLastStartDateTime", endDate);
    }
}

export function saveLastStartDateTimeInLocalStorage(startDateTime, allDateTimeDataArray) {
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

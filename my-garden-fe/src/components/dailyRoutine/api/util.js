/**
 * 일자를 받아서 해당 일자의 시작시간과 종료시간을 반환한다.
 *
 * @param targetDate 대상 일자
 * @returns {{targetStartDateTime: string, targetEndDateTime: string}} 대상 일자의 시작시간과 종료시간
 */
export function getTargetDateTimeRange(targetDate) {
    const targetStartDateTime = `${targetDate}T00:00:00`;
    const targetEndDateTime = `${targetDate}T23:59:59`;

    return {targetStartDateTime, targetEndDateTime};
}

/**
 * 해당 하는 일자가 오늘인지 확인한다.
 *
 * @param targetDate 확인할 대상 일자
 * @returns {boolean} 대상 일자가 오늘인지 여부
 */
export function isToday(targetDate) {
    return getTodayDate() === targetDate.split('T')[0];
}

/**
 * 오늘 날짜를 반환한다.
 *
 * @returns {string} 오늘 날짜
 */
export function getTodayDate() {
    return convertDateFormat(new Date());
}

/**
 * 일자를 받아서 특정 포맷으로 변환한다.
 *
 * @param date 변환할 일자
 * @returns {string} 변환된 일자 (yyyy-MM-dd)
 */
export function convertDateFormat(date) {
    if (date === null) {
        date = new Date();
    }

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
}

/**
 * 현재 시간을 기준으로 1년 전의 일자를 특정 포맷으로 반환한다.
 *
 * @returns {string} 1년 전의 일자 (yyyy-MM-dd)
 */
export function getOneYearAgoDate() {
    const currentDate = new Date();

    currentDate.setFullYear(currentDate.getFullYear() - 1);

    const year = currentDate.getFullYear();
    const month = String(currentDate.getMonth() + 1).padStart(2, '0');
    const day = String(currentDate.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
}

/**
 * 시간 문자열을 받으면 댓글 작성 시간 포맷으로 변환한다.
 *
 * @param dateTimeStr 변환할 시간 문자열
 * @returns {string} 댓글 작성 시간 포맷으로 변환된 시간 문자열
 */
export function convertCommentDateTimeFormat(dateTimeStr) {
    const date = new Date(dateTimeStr);

    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}`;
}

/**
 * 해당 시간을 분으로 변환한다.
 *
 * @param time 변환할 시간
 * @returns {number} 분으로 변환된 시간
 */
export function timeToMinutes(time) {
    const [hours, minutes] = time.split(':').map(Number);
    return hours * 60 + minutes;
}

/**
 * 해당 일자에서 시간을 추출한다.
 *
 * @param dateTime 추출할 일자
 * @returns {*} 추출된 시간
 */
export function extractTime(dateTime) {
    return dateTime.split('T')[1].slice(0, 5);
}

/**
 * 종료 시간이 현재 저장된 마지막 시작 시간보다 늦은지 확인하고 늦다면 마지막 시작 시간을 업데이트한다.<p>
 * (하루 일과에서 새로운 일과를 등록할 때, 자연스럽게 마지막에 등록된 종료 시간을 다음 일과의 시작 시간으로 사용하기 위함)
 *
 * @param endDate 종료 시간
 */
export function updateLastStartDateTime(endDate) {
    const todayLastStartDateTime = localStorage.getItem("todayLastStartDateTime");

    if (todayLastStartDateTime < endDate) {
        localStorage.setItem("todayLastStartDateTime", endDate);
    }
}

/**
 * 마지막 시작 시간을 LocalStorage에 저장한다.<p>
 * (localStorage에 저장된 마지막 시작 시간이 새로 계산된 마지막 시작 시간과 다르다면, 페이지를 새로고침하여 allDateTimeData를 다시 받아오게 한다.)
 *
 * @param startDateTime 시작 시간
 * @param allDateTimeDataArray 모든 일과 데이터
 */
export function saveLastStartDateTimeInLocalStorage(startDateTime, allDateTimeDataArray) {
    const saveStartDateTime = calculateTodayLastStartDateTime(startDateTime, allDateTimeDataArray);

    if (localStorage.getItem('todayLastStartDateTime') !== saveStartDateTime) {
        localStorage.setItem('todayLastStartDateTime', saveStartDateTime);
        location.reload();
    }
}

/**
 * 오늘의 마지막 시작 시간을 계산한다.<p>
 * (받아온 allDateTimeData가 비어있지 않다면, 해당 데이터의 마지막 종료 시간을 반환한다.)
 *
 * @param todayStartDateTime 오늘의 시작 시간
 * @param allDateTimeData 모든 일과 데이터
 * @returns {*} 오늘의 마지막 시작 시간
 */
function calculateTodayLastStartDateTime(todayStartDateTime, allDateTimeData) {
    let returnValue = todayStartDateTime;

    if (allDateTimeData.length !== 0) {
        returnValue = allDateTimeData[allDateTimeData.length - 1].endDateTime;
    }

    return returnValue;
}

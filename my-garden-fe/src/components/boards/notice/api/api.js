import axios from "axios";

export function getNoticeBoardListApi(parameters) {
    let queryParameter = '';
    if (parameters) {
        queryParameter = Object.entries(parameters)
            .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
            .join('&');
    }
    
    return axios.get(`/api/boards/notice/list?${queryParameter}`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('공지사항 목록을 불러오는데 실패했습니다.')
        });
}

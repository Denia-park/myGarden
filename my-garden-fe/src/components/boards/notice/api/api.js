import axios from "axios";

export function getNoticeBoardListApi(queryParameter) {
    return axios.get(`/api/boards/notice/list`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('공지사항 목록을 불러오는데 실패했습니다.')
        });
}

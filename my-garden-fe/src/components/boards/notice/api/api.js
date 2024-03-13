import axios from "axios";

export function getNoticeImportantBoardListApi() {
    return axios.get('/api/boards/notice/important')
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('중요 공지사항 목록을 불러오는데 실패했습니다.')
        });
}

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

export function getNoticeBoardCategoryApi(boardType) {
    return axios.get(`/api/boards/categories?boardType=${boardType}`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('공지사항 카테고리를 불러오는데 실패했습니다.')
        });
}

export function getNoticeBoardViewApi(boardId) {
    return axios.get(`/api/boards/notice/${boardId}`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('공지사항을 불러오는데 실패했습니다.')
        });
}

export function deleteNoticeBoardApi(boardId) {
    return axios.delete(`/api/boards/notice/${boardId}`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('공지사항을 삭제하는데 실패했습니다.')
        });
}

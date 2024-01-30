import axios from "axios";
import {router} from "@/scripts/router.js";

export function postBoardApi(boardType, board) {
    return axios.post(`/api/boards/${boardType}`, board)
        .then(() => {
            alert("등록되었습니다.");
            router.push(`/boards/${boardType}`);
        })
        .catch(error => {
            alert("등록에 실패했습니다.");
        });
}

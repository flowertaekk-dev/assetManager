import axios from 'axios'

const customAxios = (url, callback) => {

    axios(
        {
            url: '/api/v1' + url,
            method: 'post',
            /*
             * 개발 환경에서는 CORS 해결을 위해 아래 두 줄을 추가
             * 운영 환경에 배포할 때는 아래 두 줄 주석처리 할 것!
             */
            baseURL: 'http://localhost:8080',
            withCredentials: true,
        }
    ).then(response => {
        callback(response.data)
    })

}

export default customAxios
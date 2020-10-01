import axios from 'axios'

const customAxios = (url, callback, contents = {}) => {

    axios(
        {
            url: '/api/v1' + url,
            method: 'post',
            data: contents,
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

export const customAxiosWithResponse =  async (url, contents = {}) => {

    const response = await axios(
        {
            url: '/api/v1' + url,
            method: 'post',
            data: contents,
            /*
             * 개발 환경에서는 CORS 해결을 위해 아래 두 줄을 추가
             * 운영 환경에 배포할 때는 아래 두 줄 주석처리 할 것!
             */
            baseURL: 'http://localhost:8080',
            withCredentials: true,
        }
    )
    
    return response
}

export default customAxios
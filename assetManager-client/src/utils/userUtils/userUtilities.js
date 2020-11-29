import crypto from 'crypto'

import customAxios from '../../customAxios'
import { BYTE_LENGTH, ENCODING_TYPE, ENCRYPT_TYPE, REPEAT_COUNT } from './encryptUtils'

/**
 * 암호화를 위한 salt키를 요청
 *
 * @param {string} id
 * @returns {Promise} Promise Object
 */
export const querySalt = (id) => {
    return new Promise((resolve, reject) => {
        customAxios('/requestSalt', (response) => {
            if (response.resultStatus === 'FAILURE') {
                alert(response.reason)
                reject(response.reason)
            }

            resolve(response.salt)
        }, {
            id
        })
    })
}

/**
 * Salt 키 생성
 */
export const createSalt = () => {
    return new Promise((resolve, reject) => {
        crypto.randomBytes(64, (err, buffer) => {
            resolve(buffer.toString(ENCODING_TYPE))
        })
    })

}

/**
 * 비밀번호 암호화
 *
 * @param {string} salt
 * @param {string} password
 * @returns {Promise} Promise Object
 */
export const encryptPassword = (salt, password) => {
    return new Promise((resolve, reject) => {
        crypto.pbkdf2(password, salt, REPEAT_COUNT, BYTE_LENGTH, ENCRYPT_TYPE, (err, key) => {
            resolve(key.toString(ENCODING_TYPE))
        })
    })
}

/**
 * 로그인
 *
 * @param {string} id
 * @param {string} password
 * @param {function} callback 로그인 성공시 콜백
 */
export const logIn = async (id, password, callback) => {

    const saltKey = await querySalt(id)
    const _password = await encryptPassword(saltKey, password)

    customAxios('/login', (response) => {
        if (response.resultStatus === 'SUCCESS') {
            callback(response.user)
        } else {
            console.log('login util fail', response)
            alert(response.reason)
        }

    }, {
        id: id,
        password: _password
    })
}

/**
 * 회원가입
 *
 * @param {Object} props
 * @param {string} id
 * @param {string} password
 * @param {string} email
 * @param {string} emailAuthCode
 */
export const signUp = async (props, id, password, email, emailAuthCode) => {
    const _salt = await createSalt()
    const _password = await encryptPassword(_salt, password)

    customAxios('/signup', (data) => {

        if (data.resultStatus === 'SUCCESS') {
            props.history.goBack()
            // props.history.replace('/login')
        } else {
            alert(data.reason)
        }

    }, {
        id,
        salt: _salt,
        password: _password,
        email,
        emailAuthCode
    })
}

export const sendAuthEmail = (emailTo) => {
    customAxios('/email/requestCode', (data) => {
        // do nothing
    }, {
        addressTo: emailTo
    })
}
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

/**
 * 패스워드 변경
 *
 * @param {string} id
 * @param {string} email
 * @param {string} newPassword
 */
export const updatePassword = async (id, email, newPassword, callback) => {
    // salt 키를 불러온다
    const salt = await querySalt(id)

    // password 해쉬화
    const updatingPassword = await encryptPassword(salt, newPassword)

    customAxios('/updatePassword', (res) => {

        if (res.resultStatus === 'SUCCESS') {
            callback()
        } else {
            alert(res.reason)
        }

    }, {
        id,
        email,
        updatingPassword
    })
}

export const deleteUser = (id, callback) => {
    customAxios('/deleteUser', (res) => {
        if (res.resultStatus === 'SUCCESS')
            alert('Account is deleted')
            callback()
    }, {
        id
    })
}

/**
 * 이메일 주소 인증 메일을 전송
 *
 * @param {string} emailTo
 */
export const sendAuthEmail = (emailTo) => {
    customAxios('/email/requestCode', (data) => {
        // do nothing
    }, {
        addressTo: emailTo
    })
}

/**
 * 패스워드 중복 체크
 *
 * @param {string} password
 * @param {string} doubleCheckPassword
 */
export const doubleCheckPassword = (password, doubleCheckPassword) => {
    if ( !doubleCheckPassword.length )
        return ''

    if ( password !== doubleCheckPassword)
        return false

    return true
}
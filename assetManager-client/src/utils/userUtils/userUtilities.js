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
import React, { useEffect, useState } from 'react'

import SettingBusiness from './SettingBusiness/SettingBusiness'
import SettingTable from './SettingTable/SettingTable'
import SettingItem from './SettingItem/SettingItem'

import customAxios from '../customAxios'
import useStore from '../mobx/useStore'


import './Setting.css'

const Setting = () => {

    const { loginUser } = useStore()

    const [ businessNames, setBusinessNames ] = useState({})

    useEffect(() => {

        customAxios("/business/readAll", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                setBusinessNames(response.businessNames)
            } else {
                alert('ERROR', response.reason)
            }
        }, {
            userId: loginUser.loginUserId
        })

    }, [])

    return (
        <section className='Setting'>

            {/* 상호명 설정 및 선택 */}
            <SettingBusiness businessNames={businessNames} />
            
            {/* Table 개수 설정 */}
            <SettingTable />

            {/* 메뉴 설정: 메뉴명 단가 */}
            <SettingItem />
            
        </section>
    )
}

export default Setting
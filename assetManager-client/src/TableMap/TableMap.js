import React, { useEffect, useState } from 'react'
import _ from 'lodash'

import Table from '../Table/Table'
import useStore from '../mobx/useStore'
import customAxios from '../customAxios'
import { getAccountBook } from '../utils/localStorageManager'

import './TableMap.css'

const TableMap = () => {

    const { loginUser, selectedBusiness } = useStore()

    const [ theNumberOfTables, setTheNumberOfTables ] = useState(0)
    const [ menus, setMenus ] = useState([])
    const [ isRendering, setRenderingStatus ] = useState(true)

    useEffect(() => {
        // 선택된 상호명이 있을경우에만 세팅
        if (selectedBusiness.selectedBusinessId) {
            const accountBook = getAccountBook()
            const currentAccountBook = accountBook[selectedBusiness.selectedBusinessId]

            setTheNumberOfTables(_.size(currentAccountBook))
        }
        setRenderingStatus(false)
    }, [ selectedBusiness.selectedBusinessId ])

    useEffect(() => {
        customAxios("/menu/readAll", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                // 메뉴 저장
                setMenus(response.menus)

            } else {
                alert('ERROR', response.reason)
            }
        }, {
            userId: loginUser.loginUserId,
            businessId: selectedBusiness.selectedBusinessId
        })
    }, [])

    // ----------------------------------------------------------
    // utils

    /**
     * Table 컴포넌트를 반환한다
     *
     * @param {string} key
     * @param {string} tableId
     * @param {string} tableTitle
     * @param {Array} menus
     * @param {Boolean} isLast
     */
    const createTableComponent = (key, tableId, tableTitle, menus, isLast) => {
        return <Table
            key={ key }
            tableId={ tableId }
            tableTitle={ tableTitle }
            menus={ menus }
            isLast={ isLast } />
    }

    /**
     * 설정된 테이블 수 만큼의 테이블을 랜더링
     *
     * @param {Number} theNumberOfTables
     */
    const renderTables = ( theNumberOfTables ) => {
        const tables = []

        // 설정된 테이블 수가 '0' 이라면, '주문내역'으로 테이블 하나 추가
        if (theNumberOfTables === 0) {
            tables.push(createTableComponent(1, 0, '주문내역', menus, true))
            return tables
        }

        // 설정된 테이블 수가 0이 아니면 Table 여러개 생성
        for (let i=0; i < theNumberOfTables; i++) {
            let shownTableNumber = i + 1
            tables.push(
                createTableComponent(
                    shownTableNumber,                           // key
                     i,                                         // tableId
                     `Table${shownTableNumber}`,                // tableTitle
                     menus,                                     // menus
                     shownTableNumber === theNumberOfTables)    // isLast
            )
        }

        return tables
    }

    return (
        <section className={`TableMap${!selectedBusiness.selectedBusinessId ? '__empty' : ''}`}>
            {
                ( selectedBusiness.selectedBusinessId && !isRendering )
                    && renderTables(theNumberOfTables)
            }
            {
                ( !selectedBusiness.selectedBusinessId ) &&
                    <h2>상호명을 등록 및 선택해주세요</h2>
            }
        </section>
    )
}

export default TableMap
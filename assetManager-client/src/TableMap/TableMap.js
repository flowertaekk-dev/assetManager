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

    /**
     * 설정된 테이블 수 만큼의 테이블을 랜더링
     */
    const renderTables = ( theNumberOfTables ) => {
        const tables = []

        for (let i=0; i < theNumberOfTables; i++) {
            let shownTableNumber = i + 1

            tables.push(
                <Table
                    key={ shownTableNumber }
                    tableId={ i }
                    tableTitle={ `Table${shownTableNumber}` }
                    menus={ menus }
                    isLast={ shownTableNumber === theNumberOfTables } />
            )
        }

        // 설정된 테이블 수가 '0' 이라면, '주문내역'으로 테이블 하나 추가
        if (tables.length === 0) {
            tables.push(
                <Table
                    key={ 1 }
                    tableId={ 0 }
                    tableTitle={ `주문내역` }
                    menus={ menus }
                    isLast={ true } />
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
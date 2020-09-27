import React, { useEffect, useState } from 'react'

import Table from '../Table/Table'
import useStore from '../mobx/useStore';
import customAxios from '../customAxios'

import './TableMap.css'

const TableMap = () => {

    const { loginUser, selectedBusiness } = useStore()

    const [ theNumberOfTables, setTheNumberOfTables ] = useState(0)

    useEffect(() => {
        customAxios("/table/read", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                // 카운트 저장
                setTheNumberOfTables(response.tableCount)
            } else {
                alert('ERROR', response.reason)
            }
        }, {
            userId: loginUser.loginUserId,
            businessId: selectedBusiness.selectedBusinessId
        })
    }, [selectedBusiness.selectedBusinessId])

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
                    tableTitle={ `Table${shownTableNumber}` }
                    isLast={ shownTableNumber === theNumberOfTables } />
            )
        }

        // 설정된 테이블 수가 '0' 이라면, '주문내역'으로 테이블 하나 추가
        if (tables.length === 0) {
            tables.push(
                <Table
                    key={0}
                    tableTitle={`주문내역`} />
            )
        }

        return tables
    }

    return (
        <section className='TableMap'>
            { renderTables(theNumberOfTables) }
        </section>
    )
}

export default TableMap
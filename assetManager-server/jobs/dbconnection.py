#! /usr/bin/python3.8

import pymysql.cursors


def getMariaDBConnection():
    return pymysql.connect(
        host='localhost',
        port=3306,
        user='flowertaekk',
        password='pass',
        db='assetManagerDev',
        charset='utf8mb4')

def selectAll(sql, variables):
    conn = getMariaDBConnection()

    try:
        with conn.cursor() as cursor:
            cursor.execute(sql, variables)
            return cursor.fetchall()
    finally:
        conn.close()

def cud(sql, variables):
    conn = getMariaDBConnection()

    try:
        with conn.cursor() as cursor:
            cursor.execute(sql, variables)
            return True
    except Exception as err:
        print('Error: {0}'.format(err))
        return False
    finally:
        conn.commit()
        conn.close()
#! /usr/bin/python3.8

import unittest
from datetime import datetime, timedelta
from dbconnection import selectAll, cud

class ClearEmailAuthTest(unittest.TestCase):

    def setUp(self):
        cud("DELETE FROM `email_auth`", ())
        print('init done.')

    def tearDown(self):
        print('do nothing')

    # it is test for test db (watch out.. it deletes all your test data)
    def test_can_delete_older_than_5mins_data (self):
        print('start test_can_delete_older_than_5mins_data...')

        # get current time
        now = datetime.now()

        # given
        self._insertEmailAuth((now - timedelta( minutes = 7), now - timedelta( minutes = 7), 'testAuth', 'test1@test.com', '발송완료'))
        self._insertEmailAuth((now - timedelta( minutes = 6), now - timedelta( minutes = 6), 'testAuth', 'test2@test.com', '발송완료'))
        self._insertEmailAuth((now - timedelta( minutes = 4), now - timedelta( minutes = 4), 'testAuth', 'test3@test.com', '발송완료'))

        # when
        with open('clear_email_auth.py', 'r') as cmd:
            exec(cmd.read())

        # then
        result = selectAll("SELECT * FROM `email_auth`", ())
        self.assertTrue(result)
        self.assertEqual(len(result), 1)

        print('end test_can_delete_older_than_5mins_data...')

    ####################################################################################
    ####################################################################################
    ####################################################################################

    def _insertEmailAuth(self, variables):
        cud(
            '''
            INSERT INTO `email_auth` (`created_time`, `modified_time`, `auth_code`, `email`, `status`)
            VALUES (%s, %s, %s, %s, %s)
            ''', variables)


if __name__ == "__main__":
    unittest.main()

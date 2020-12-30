#! /usr/bin/python3.8

from datetime import datetime, timedelta
from dbconnection import cud


def main():
    if __name__ == '__main__':
        print ('Start clear_email_auth.py...')

        # get current time
        now = datetime.now()
        condition = now - timedelta(minutes=5)

        print('delete all data before {0}'.format(condition))

        # delete
        result = cud("DELETE FROM `email_auth` WHERE created_time < %s", (condition))
        if result is not True:
            raise RuntimeError('Something went wrong: failed to delete old email_auth data')

        print ('End clear_email_auth.py...')

if __name__ == '__main__':
    main()

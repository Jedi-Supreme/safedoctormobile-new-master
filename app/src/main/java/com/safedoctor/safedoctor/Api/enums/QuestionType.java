package com.safedoctor.safedoctor.Api.enums;

/**
 * Created by Stevkkys on 9/19/2017.
 */

public enum QuestionType
{
    YES_NO_NOLIST( 1 ),
    FINITE_LIST( 2 ),
    INFINITE_LIST( 3 ),
    YES_NO_WITH_FINITELIST( 4),
    YES_NO_WITH_INFINITELIST(5);

    private final int number;

    QuestionType(int number)
    {
        this.number  = number;
    }


    public int getNumber()
    {
        return number;
    }

    public static QuestionType fromValue( int value )
    {
        for ( QuestionType status : QuestionType.values() )
        {
            if ( status.number == value)
            {
                return status;
            }
        }

        return null;
    }
}

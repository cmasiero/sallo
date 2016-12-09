package it.cristiano.sallo.dao

/**
  * Created by cristiano on 09/12/16.
  */
sealed class ApplicationException extends Exception
object IndexPositionException extends ApplicationException

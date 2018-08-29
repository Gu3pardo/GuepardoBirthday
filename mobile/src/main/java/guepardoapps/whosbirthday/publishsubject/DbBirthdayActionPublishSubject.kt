package guepardoapps.whosbirthday.publishsubject

import guepardoapps.whosbirthday.enum.DbBirthdayAction
import io.reactivex.subjects.PublishSubject

internal class DbBirthdayActionPublishSubject private constructor() {

    private object Holder {
        val instance: DbBirthdayActionPublishSubject = DbBirthdayActionPublishSubject()
    }

    companion object {
        val instance: DbBirthdayActionPublishSubject by lazy { Holder.instance }
    }

    val publishSubject = PublishSubject.create<DbBirthdayAction>()!!
}
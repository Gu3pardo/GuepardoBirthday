package guepardoapps.whosbirthday.customadapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.afollestad.materialdialogs.MaterialDialog
import com.plattysoft.leonids.ParticleSystem
import com.rey.material.widget.FloatingActionButton
import guepardoapps.whosbirthday.R
import guepardoapps.whosbirthday.activities.ActivityEdit
import guepardoapps.whosbirthday.controller.BirthdayController
import guepardoapps.whosbirthday.controller.NavigationController
import guepardoapps.whosbirthday.database.birthday.DbBirthday
import guepardoapps.whosbirthday.model.Birthday

@ExperimentalUnsignedTypes
internal class BirthdayListAdapter(private val context: Context, private val list: Array<Birthday>) : BaseAdapter() {

    private val navigationController: NavigationController = NavigationController(context)

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private class Holder {
        lateinit var remindMe: CheckBox
    }

    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Birthday = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("SetTextI18n", "ViewHolder", "InflateParams")
    override fun getView(index: Int, convertView: View?, parentView: ViewGroup?): View {
        val rowView: View = inflater.inflate(R.layout.list_item, null)
        val birthday = list[index]

        Holder().apply {
            rowView.findViewById<TextView>(R.id.birthday_item_name).text = birthday.name
            rowView.findViewById<TextView>(R.id.birthday_item_group).text = birthday.group
            rowView.findViewById<TextView>(R.id.birthday_item_date).text = birthday.dateText
            rowView.findViewById<TextView>(R.id.birthday_item_age).text = "${birthday.age}"

            remindMe = rowView.findViewById(R.id.birthday_remind_me)
            remindMe.isChecked = birthday.remindMe
            remindMe.setOnCheckedChangeListener { _, isChecked ->
                birthday.remindMe = isChecked
                DbBirthday(context).update(birthday)
                BirthdayController().checkForBirthday(context)
            }

            rowView.findViewById<FloatingActionButton>(R.id.btnEdit).setOnClickListener {
                val bundle = Bundle()
                bundle.putString(context.getString(R.string.bundleDataId), birthday.id)
                navigationController.navigateWithData(ActivityEdit::class.java, bundle, false)
            }

            rowView.findViewById<FloatingActionButton>(R.id.btnDelete).setOnClickListener {
                MaterialDialog(context).show {
                    title(text = context.getString(R.string.delete))
                    message(text = String.format(context.getString(R.string.deleteRequest), birthday.name))
                    positiveButton(text = context.getString(R.string.yes)) { DbBirthday(context).delete(birthday.id) }
                    negativeButton(text = context.getString(R.string.no))
                }
            }
        }

        if (birthday.hasBirthday) {
            rowView.setBackgroundColor(context.getColor(R.color.colorPrimaryDark))
            ParticleSystem(
                    context as Activity,
                    context.resources.getInteger(R.integer.maxParticles),
                    R.drawable.particle,
                    context.resources.getInteger(R.integer.timeToLive).toLong())
                    .setSpeedRange(0.2f, 0.5f)
                    .oneShot(rowView, context.resources.getInteger(R.integer.maxParticles))
        }

        return rowView
    }
}
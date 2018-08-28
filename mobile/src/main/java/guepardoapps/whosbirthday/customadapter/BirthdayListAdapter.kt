package guepardoapps.whosbirthday.customadapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.plattysoft.leonids.ParticleSystem
import guepardoapps.whosbirthday.R
import guepardoapps.whosbirthday.activities.ActivityDetails
import guepardoapps.whosbirthday.common.Constants
import guepardoapps.whosbirthday.controller.NavigationController
import guepardoapps.whosbirthday.database.birthday.DbBirthday
import guepardoapps.whosbirthday.extensions.common.integerFormat
import guepardoapps.whosbirthday.model.Birthday

class BirthdayListAdapter(private val context: Context, private val list: Array<Birthday>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val navigationController = NavigationController(context)

    private class Holder {
        lateinit var name: TextView
        lateinit var group: TextView
        lateinit var date: TextView
        lateinit var image: ImageView
        lateinit var remindMe: CheckBox
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return list[position].id
    }

    override fun getCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n", "ViewHolder", "InflateParams")
    override fun getView(index: Int, convertView: View?, parentView: ViewGroup?): View {
        val rowView: View = inflater.inflate(R.layout.list_item, null)

        val birthday = list[index]

        val holder = Holder()
        holder.name = rowView.findViewById(R.id.birthday_item_name)
        holder.group = rowView.findViewById(R.id.birthday_item_group)
        holder.date = rowView.findViewById(R.id.birthday_item_date)
        holder.image = rowView.findViewById(R.id.birthday_item_image)
        holder.remindMe = rowView.findViewById(R.id.birthday_remind_me)

        holder.name.text = birthday.name
        holder.group.text = birthday.group
        holder.date.text = "${birthday.day.integerFormat(2)}.${birthday.month.integerFormat(2)}.${birthday.year.integerFormat(4)}"

        holder.remindMe.isChecked = birthday.remindMe
        holder.remindMe.setOnCheckedChangeListener { _, isChecked ->
            birthday.remindMe = isChecked
            DbBirthday(context).update(birthday)
        }

        if (birthday.hasBirthday()) {
            holder.image.visibility = View.VISIBLE
            rowView.setBackgroundColor(context.getColor(R.color.colorPrimaryDark))
            ParticleSystem(
                    context as Activity,
                    150,
                    R.drawable.particle,
                    750)
                    .setSpeedRange(0.2f, 0.5f)
                    .oneShot(rowView, 150)
        }

        rowView.setOnClickListener {
            val details = Bundle()
            details.putLong(Constants.bundleDataId, birthday.id)
            navigationController.navigateWithData(ActivityDetails::class.java, details, false)
        }

        return rowView
    }
}
package com.example.android.ecotown.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.ecotown.Adapters.CheckListAdapter
import com.example.android.ecotown.Models.Check
import com.example.android.ecotown.databinding.ActivityCheckListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CheckListActivity : AppCompatActivity() {
    private lateinit var bindingCheckList: ActivityCheckListBinding

    private lateinit var checkList: MutableList<Check>

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private val key = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val keyPushList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingCheckList = ActivityCheckListBinding.inflate(layoutInflater)
        setContentView(bindingCheckList.root)


        database = FirebaseDatabase.getInstance()


        databaseReference = database.getReference("Check").child(key)

        val layoutManager = LinearLayoutManager(applicationContext)
        bindingCheckList.recycler.layoutManager = layoutManager


        val checkListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("Vera", "loadPost:onCancelled", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                checkList = mutableListOf()
                for (postSnap: DataSnapshot in p0.children) {
                    val check = postSnap.getValue(Check::class.java)
                    checkList.add(check!!)
                }
                if (checkList.isEmpty()) {
                    addCheck()
                    for (postSnap: DataSnapshot in p0.children) {
                        val check = postSnap.getValue(Check::class.java)
                        checkList.add(check!!)
                    }
                }


                val checkAdapter = CheckListAdapter(checkList)


                checkAdapter.ischecked.observe(this@CheckListActivity, Observer {
                    val state = mutableMapOf<String, Any>()
                    state["state"] = it
                    val pos = checkAdapter.pos
                    val newRef = databaseReference.child(keyPushList[pos]) //РАЗОБРАТЬСЯ С КЛЮЧОМ

                    newRef.updateChildren(state)


                })
                bindingCheckList.recycler.adapter = checkAdapter

            }
        }
        databaseReference.addValueEventListener(checkListener)
    }


    private fun addCheck() {
        val list2 = mutableListOf<Check>()
        list2.add(Check("Выключайте воду когда чистите зубы", false))
        list2.add(
            Check(
                "Больше ходите пешком, либо используйте общественный транспорт",
                false
            )
        )
        list2.add(Check("Сажайте деревья", false))
        list2.add(
            Check(
                "Вместо полиэтиленовой пленки используйте восковую салфетку",
                false
            )
        )
        list2.add(Check("Скажите «Нет» одноразовым салфеткам", false))
        list2.add(Check("Покупайте товары без упаковки", false))
        list2.add(Check("Используйте натуральные средства вместо химии", false))
        list2.add(Check("Поддерживайте экологичные бренды", false))
        list2.add(Check("Сдавайте вещи в секонд-хэнды", false))
        list2.add(Check("Покупайте вещи в секонд-хэндах", false))
        list2.add(Check("Утилизируйте батарейки", false))
        list2.add(Check("Не выбрасывайте мусор на улице", false))
        list2.add(Check("Разделяйте мусор", false))
        list2.add(
            Check(
                "Обращайте внимание на вещи из вторично переработанных продуктов",
                false
            )
        )
        list2.add(
            Check(
                "Используйте термокружки и многоразовые бутылочки",
                false
            )
        )
        list2.add(
            Check(
                "Минимизируйте количество полиэтиленовых пакетов в быту",
                false
            )
        )
        list2.add(Check("Ходите в магазин со своим пакетом", false))
        list2.add(Check("Не покупайте новогодние елки", false))
        list2.add(
            Check(
                "Для новогодних подарков преобретите многоразовую упаковку",
                false
            )
        )
        list2.add(Check("Для покупки фруктов преобретите фруктовки", false))
        list2.add(
            Check(
                "Потребляйте разумно, покупайте только необходимые вещи",
                false
            )
        )
        list2.add(Check("Пользуйтесь пунктами приема вторсырья и отходов", false))
        list2.add(
            Check(
                "Используйте только экологичную бытовую химию и косметику",
                false
            )
        )
        list2.add(Check("Экономьте воду, газ и электричество", false))

        for (item in list2) {
            val reference: DatabaseReference =
                database.getReference("Check").child(key).push()
            keyPushList.add(reference.key.toString())  // ДОДУМАТЬ КАК ВЫТАЩИТЬ В ОБЩИЙ ДОСТУП, ВОЗМОЖНО ВСТАВИТЬ В БАЗУ ДАННЫХ И ЧЕРЕЗ УЖЕ ДОСТУПНЫЙ КЛЮЧ ВЫТАСКИВАТЬ
            reference.setValue(item).addOnSuccessListener {

            }

        }

    }


}

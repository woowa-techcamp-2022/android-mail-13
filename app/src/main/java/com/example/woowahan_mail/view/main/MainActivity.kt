package com.example.woowahan_mail.view.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import com.example.woowahan_mail.R
import com.example.woowahan_mail.databinding.ActivityMainBinding
import com.example.woowahan_mail.view.getDeviceWidth
import com.example.woowahan_mail.view.main.mail.MailFragment
import com.example.woowahan_mail.view.main.setting.SettingFragment
import com.example.woowahan_mail.view.setDrawerIconColor

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val settingFragment = SettingFragment()

    private val viewModel: MainViewModel by viewModels()

    private val fm: FragmentManager by lazy { supportFragmentManager }

    private val menuItemClickListener: (MenuItem) -> Boolean = { menuItem ->
        if (menuItem.itemId == R.id.item_main_menu_mail) {
            showMailFragment()
            viewModel.currentFocus = SELECTED_MAIL
            true
        } else if (menuItem.itemId == R.id.item_main_menu_setting) {
            showSettingFragment()
            viewModel.currentFocus = SELECTED_SETTING
            viewModel.currentMail = MailFragment.PRIMARY
            setDrawerIconColor(viewModel.currentMail)
            true
        } else {
            false
        }
    }

    private val drawerMenuItemClickListener: (MenuItem) -> Boolean = { menuItem ->
        viewModel.currentFocus = SELECTED_MAIL
        binding.drawerMainContainer.closeDrawer(GravityCompat.START)
        when (menuItem.itemId) {
            R.id.item_drawer_menu_primary -> {
                viewModel.currentMail = MailFragment.PRIMARY
                setDrawerIconColor(MailFragment.PRIMARY)
                showMailFragment()
                true
            }
            R.id.item_drawer_menu_social -> {
                viewModel.currentMail = MailFragment.SOCIAL
                setDrawerIconColor(MailFragment.SOCIAL)
                showMailFragment()
                true
            }
            R.id.item_drawer_menu_promotions -> {
                viewModel.currentMail = MailFragment.PROMOTIONS
                setDrawerIconColor(MailFragment.PROMOTIONS)
                showMailFragment()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDrawerIconColor(viewModel.currentMail)
        setEmailAndName()
        setProperView()
        setOnClickListeners()
    }

    private fun setEmailAndName(){
        viewModel.name = intent.getStringExtra(this.getString(R.string.name))!!
        viewModel.email = intent.getStringExtra(this.getString(R.string.email))!!
    }

    private fun setProperView() {
        if (getDeviceWidth() > 600) {
            setNavigationRail()
        } else {
            setBottomNavigation()
        }
    }

    private fun setOnClickListeners() {
        binding.imgToolbarMainMore.setOnClickListener { binding.drawerMainContainer.openDrawer(GravityCompat.START) }
        binding.navigationMainDrawer.setNavigationItemSelectedListener(drawerMenuItemClickListener)
    }

    private fun setNavigationRail() {
        initNavigationRailMenu()
        setNavigationRailSelectedIcon()
        setNavigationRailClickListener()
    }

    private fun setBottomNavigation() {
        initBottomNavigationMenu()
        setBottomNavigationSelectedIcon()
        setBottomNavigationClickListener()
    }

    private fun initNavigationRailMenu() {
        binding.bottomNavigationMain.visibility = View.GONE
        binding.navigationRailMain.apply {
            visibility = View.VISIBLE
            menu.clear()
            inflateMenu(R.menu.main_menu)
        }
    }

    private fun setNavigationRailSelectedIcon() {
        if (viewModel.currentFocus == SELECTED_MAIL) {
            binding.navigationRailMain.menu.findItem(R.id.item_main_menu_mail).isChecked = true
        }
        else {
            binding.navigationRailMain.menu.findItem(R.id.item_main_menu_setting).isChecked = true
        }
    }

    private fun setNavigationRailClickListener() {
        binding.navigationRailMain.setOnItemSelectedListener(menuItemClickListener)
    }

    private fun initBottomNavigationMenu() {
        binding.navigationRailMain.visibility = View.GONE
        binding.bottomNavigationMain.apply {
            visibility = View.VISIBLE
            menu.clear()
            inflateMenu(R.menu.main_menu)
        }
    }

    private fun setBottomNavigationSelectedIcon() {
        if (viewModel.currentFocus == SELECTED_MAIL) {
            binding.bottomNavigationMain.menu.findItem(R.id.item_main_menu_mail).isChecked = true
        } else {
            binding.bottomNavigationMain.menu.findItem(R.id.item_main_menu_setting).isChecked = true
        }
    }

    private fun setBottomNavigationClickListener() {
        binding.bottomNavigationMain.setOnItemSelectedListener(menuItemClickListener)
    }

    private fun showMailFragment() {
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        fm.beginTransaction().replace(R.id.fragment_container_main, MailFragment()).commit()
    }

    private fun showSettingFragment() {
        val tag = this.getString(R.string.setting_fragment)
        if (fm.backStackEntryCount == 0) {
            fm.beginTransaction().replace(R.id.fragment_container_main, settingFragment, tag).addToBackStack(tag).commit()
        }
        else if (fm.getBackStackEntryAt(fm.backStackEntryCount - 1).name == tag)
        else {
            fm.popBackStack()
            fm.beginTransaction().replace(R.id.fragment_container_main, settingFragment, tag).addToBackStack(tag).commit()
        }
    }

    companion object {
        const val SELECTED_MAIL = 1
        const val SELECTED_SETTING = 2
    }
}
package com.shopme.admin.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;

@Service
public class SettingService {
	@Autowired
	private SettingRepository repo;

	public List<Setting> listAllSettings() {

		return (List<Setting>) repo.findAll();
	}

	public GeneralSettingBag getGeneralSettings() {
		List<Setting> settings = new ArrayList<>();

		List<Setting> generalSettings = repo.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySettings = repo.findByCategory(SettingCategory.CURRENCY);

		settings.addAll(generalSettings);
		settings.addAll(currencySettings);

		return new GeneralSettingBag(settings);
	}
	
	public void saveAll(Iterable<Setting> settings) {
		repo.saveAll(settings);
	}

	public List<Setting> getMailServerSettings() {
		return repo.findByCategory(SettingCategory.MAIL_SERVER);
	}
	
	public List<Setting> getPaymentServerSettings(){
		return repo.findByCategory(SettingCategory.PAYMENT);
	}

	public List<Setting> getMailTemplateSettings() {
		return repo.findByCategory(SettingCategory.MAIL_TEMPLATES);
	}

	public List<Setting> getCurrencySetting() {
		List<Setting> currencySettings = (List<Setting>) repo.findAll();
		List<Setting> listSettings = new ArrayList<>();
		for (Setting setting : currencySettings) {
			if (setting.getKey().equals("CURRENCY_SYMBOL") || setting.getKey().equals("THOUSANDS_POINT_TYPE")
					|| setting.getKey().equals("DECIMAL_DIGITS") || setting.getKey().equals("DECIMAL_POINT_TYPE")
					|| setting.getKey().equals("CURRENCY_SYMBOL_POSITION"))
				listSettings.add(setting);

		}
		return listSettings;
	}

}

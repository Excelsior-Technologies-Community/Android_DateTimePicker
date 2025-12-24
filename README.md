# **DateTime Picker & Wheelview**

---
A customizable **Wheel-style Date and Time Picker** for Android, supporting XML attributes for colors, text size, and font. Includes dynamic coloring of selected wheel item and OK/Cancel buttons in dialogs.

---

## ✨ **Features**

- Wheel-style selection for dates and time.

- Supports custom text color, size, and font.

- Selected item highlights with custom color.

- Customize OK/Cancel button colors in the dialogs.

- AM/PM selection for time picker.

- Easily configurable via XML attributes.

- Default system colors used when attributes are not provided (e.g., default purple for NumberPicker).



  ---

# **Preview**
---
<p align="center">
  <img src="https://github.com/user-attachments/assets/828a9019-a434-4424-83cb-31bac45df6a0"
       alt="Demo GIF"
       width="200">


</p>


## ⚡ **Installation**

**Step 1:** Add JitPack repository to your root build.gradle:

```gradle
maven { url = uri("https://jitpack.io") }
```

**Step 2:** Add the dependency in your app `build.gradle` (example if hosted on JitPack):  

```gradle
dependencies {
	       	        implementation 'com.github.Excelsior-Technologies-Community:Android_DateTimePicker:1.0.0'


}
```
## ⚡ **attrs file**

```

<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="DateTimePickerView">
        <attr name="dtTextColor" format="color|reference" />
        <attr name="dtTextSize" format="dimension" />
        <attr name="dtFontFamily" format="string|reference" />
        <attr name="dtShowDate" format="boolean" />
        <attr name="dtShowTime" format="boolean" />
        <attr name="dtPickerColor" format="color|reference" />
        <attr name="dtDialogButtonTextColor" format="color|reference" />
    </declare-styleable>
</resources>

```

## ⚡ **Usage**

**1. For the date wheel picker view**

```
<com.ext.android_datetimepicker.WheelDateTimePickerView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:dtTextColor="@android:color/white"
    app:dtTextSize="16sp"
    app:dtFontFamily="sans-serif"
    app:dtDialogButtonColor="@android:color/holo_blue_dark"/>


```

**2. For the time wheel picker view**
```

<com.ext.android_datetimepicker.WheelTimePickerView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:dtTextColor="@android:color/white"
    app:dtTextSize="16sp"
    app:dtFontFamily="sans-serif"
    app:dtDialogButtonColor="@android:color/holo_blue_dark"/>

```
**3. For the simple date and time picker**
```
  <com.ext.android_datetimepicker.DateTimePickerView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:dtTextSize="20dp"




        />
```


## **2. Setup in Activity**
```
val datePicker: WheelDateTimePickerView = findViewById(R.id.datePicker)
val selectedDate = datePicker.getSelectedDate()

val timePicker: WheelTimePickerView = findViewById(R.id.timePicker)
val selectedTime = timePicker.getSelectedTime()


```



## **📄 License**

**MIT License**  
```
Copyright (c) 2025 Excelsior Technologies

Permission is hereby granted, free of charge, to any person obtaining a copy  
of this software and associated documentation files (the "Software"), to deal  
in the Software without restriction, including without limitation the rights  
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  
copies of the Software, and to permit persons to whom the Software is  
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all  
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED **"AS IS"**, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR  
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,  
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
```



  

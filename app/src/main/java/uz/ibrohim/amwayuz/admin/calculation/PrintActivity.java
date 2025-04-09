package uz.ibrohim.amwayuz.admin.calculation;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.common.apiutil.printer.ThermalPrinter;
import com.common.apiutil.printer.UsbThermalPrinter;
import com.common.apiutil.util.SDKUtil;

import java.util.ArrayList;
import java.util.Objects;

import uz.ibrohim.amwayuz.R;
import uz.ibrohim.amwayuz.admin.products.ProductsItem;

public class PrintActivity extends AppCompatActivity {

    private ArrayList<ProductsItem> itemList;
    String totalPrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKUtil.getInstance(this).initSDK();
        setContentView(R.layout.activity_print);

        itemList = (ArrayList<ProductsItem>) getIntent().getSerializableExtra("items");
        totalPrice = getIntent().getStringExtra("total");

        new Thread(() -> {
            try {
                UsbThermalPrinter printer = new UsbThermalPrinter(PrintActivity.this);
                printer.reset();
                printer.setGray(4);

                // 🟢 Amway sarlavha
                printer.setAlgin(UsbThermalPrinter.ALGIN_MIDDLE);
                printer.setTextSize(33);
                printer.setBold(true);
                printer.addString("AMWAY");

                // 🔵 Har bir mahsulot ramkada
                printer.setAlgin(UsbThermalPrinter.ALGIN_LEFT);
                printer.setTextSize(23);
                printer.setGray(4);
                printer.setBold(true);

                for (ProductsItem item : itemList) {
                    String name = item.getName();
                    String price = item.getPrice();
                    String amount = item.getAmount();

                    printer.addString("---------------------------");
                    printer.addString("Номи   : " + name);
                    printer.addString("Миқдори   : " + amount);
                    printer.addString("Нархи  : " + price + " сўм");
                }

                printer.setAlgin(UsbThermalPrinter.ALGIN_LEFT);
                printer.setTextSize(23);
                printer.setGray(4);
                printer.setBold(true);
                printer.addString("---------------------------");

                // 🔴 Jami
                printer.setBold(true);
                printer.setTextSize(26);
                printer.setAlgin(UsbThermalPrinter.ALGIN_RIGHT);
                printer.addString(totalPrice + "\n");

                printer.printString();
                printer.walkPaper(10);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    ThermalPrinter.stop(PrintActivity.this);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        onBackPressed();
    }
}

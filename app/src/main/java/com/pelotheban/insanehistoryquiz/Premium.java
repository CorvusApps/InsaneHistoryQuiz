package com.pelotheban.insanehistoryquiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class Premium extends AppCompatActivity implements PurchasesUpdatedListener {

    Dialog dialog;

    private int height2;
    private int width2;

    //premium dialog and functionality
    private String showPremiumDialogToggle, showPremiumDialogToggle2, goingToRatingsInsteadToggle, goingToBadgesinsteadToggle;

    private ImageView imgPremiumAsk2X, imgPremiumAskNo2X;
    private Button btnPremiumMaybe2X, btnPremiumNo2X;

    //in-app purchases
    private BillingClient billingClient2;
    private List skuList2 = new ArrayList();
    private String sku2 = "premium_version";
    private SkuDetails skuDetails2;

    //Firebase

    private DatabaseReference gameReference;

    private String uid; // this is for the user account side
    private DatabaseReference userReference;

    //badge award
    private LinearLayout loutBadges2X;
    private ImageView imgBadgeAward2X;

    private TextView txtBadgeAward2X, txtBadgeMessageOld2;
    private String badgeAwardMsg2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        /// sizing the display to have both the question and then the answer mostly in the center

        skuList2.add(sku2);
        setupBillingClient();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        double height = size.y;

        height2 = (int) Math.round(height);
        width2 = (int) Math.round(width);

        uid = FirebaseAuth.getInstance().getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(uid);

        loutBadges2X = findViewById(R.id.loutBadges2);
        imgBadgeAward2X = findViewById(R.id.imgBadgeAward2);
        txtBadgeAward2X = findViewById(R.id.txtBadgeMessage2);
        txtBadgeMessageOld2 = findViewById(R.id.txtBadgeMessageOld2);


        premiumask2();

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                Premium.this.finish();
            }
        });


    }

    public void premiumask2() {

        LayoutInflater inflater = LayoutInflater.from(Premium.this);
        View view = inflater.inflate(R.layout.zzz_premiumask_dialog, null); // just ussing this fb dialog for ratings

        dialog = new AlertDialog.Builder(Premium.this)
                .setView(view)
                .create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        double dialogWidth = width2*.75;
        int dialogWidthFinal = (int) Math.round(dialogWidth);
        double dialogHeight = dialogWidthFinal*1.5;
        int dialogHeightFinal = (int) Math.round(dialogHeight);

        dialog.getWindow().setLayout(dialogWidthFinal, dialogHeightFinal);


        imgPremiumAsk2X = view.findViewById(R.id.imgPremiumAsk);
        //imgFBShareGlowX = view.findViewById(R.id.imgFBShareGlow);
        btnPremiumMaybe2X = view.findViewById(R.id.btnPremiumMaybe);
        btnPremiumNo2X = view.findViewById(R.id.btnPremiumNo);

        btnPremiumNo2X.setVisibility(View.GONE);
        btnPremiumMaybe2X.setText("No thank you. I changed my mind");


        btnPremiumMaybe2X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //don't want to move to maybe here because can also be maybetwo - and that is handled automatically upfront
                dialog.dismiss();
                finish();
            }
        });

//        btnPremiumNo2X.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                userReference.child("premiumasktoggle").setValue("never");
//                dialog.dismiss();
//                finish();
//
//            }
//        });



        imgPremiumAsk2X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Premium.this, "BUYING 22222", Toast.LENGTH_LONG).show();

                dialog.dismiss();
                purchasePremium();

            }
        });

    }

    public void purchasePremium(){

        //Toast.makeText(ExpandedAnswer.this, "SORRY THE PREMIUM FEATURE IS IN DEVELOPMENT AND SHOULD BE READY SHORTHLY", Toast.LENGTH_LONG).show();

        BillingFlowParams params = BillingFlowParams
                .newBuilder()
                .setSkuDetails(skuDetails2)
                .build();
        billingClient2.launchBillingFlow(Premium.this, params);
    }


    private void setupBillingClient() {

        billingClient2 = BillingClient.newBuilder(Premium.this).enablePendingPurchases().setListener(this).build();
        billingClient2.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                    //billing client set up properly so we can load SKUs
                    loadAllSKUs();


                }
            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });

    }

    private void loadAllSKUs(){

        if(billingClient2.isReady()){

            SkuDetailsParams params = SkuDetailsParams.newBuilder()
                    .setSkusList(skuList2)
                    .setType(BillingClient.SkuType.INAPP)
                    .build();

            billingClient2.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                    if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){

                        for (Object skuDetailsObject : skuDetailsList) {

                            skuDetails2 = (SkuDetails) skuDetailsObject;
                            // this was originally set up as a local variable and called final
                            // but because i don't have the functionality in the if but in a different method called from a different place made it universal - may be a problem
                            if (skuDetails2.getSku().equals(sku2)) {

                                // tutorial had the buy button activate here
                                // i think we are fine as is as long as all these methods called first on on creaet

                            } // add else ifs here if we  have other products
                        }

                    }
                }
            });
        }
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {

        int responseCode = billingResult.getResponseCode();

        if (responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {

            // no ads and no need to purchase again
            // i think this for statement is not necessary for having this one sku but keeping as it doesn't hurt and for later ref

            for (Purchase purchase : purchases) {

                handlePurchase();
            }

        } else if (responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED ) {

            // should not need this because in DB as purchased

        } else if (responseCode == BillingClient.BillingResponseCode.USER_CANCELED)  {

            finish();

        }

    }

    private void handlePurchase(){

        // set to bought which should mean the ask to buy should never be triggered again
        userReference.child("premiumasktoggle").setValue("bought");

        // In Game using this to stop interstitial and rewarded from being triggered

        // should also have reward trigger the showad; later we will have a button so that the reward add can be stopped
        //hoping this executed


        loutBadges2X.setVisibility(View.VISIBLE);
        //fabPopUpEAX.setVisibility(View.GONE);

        userReference.getRef().child("badgepre").setValue(1);

        txtBadgeAward2X.setText("Thank you for becoming a PREMIUM member! No more ADS for YOU!!!");
        txtBadgeMessageOld2.setVisibility(View.VISIBLE);



        try {

            CountDownTimer badgeAwardTimer = new CountDownTimer(5000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    dialog.dismiss();
                    finish();
                }
            }.start();

        } catch (Exception e) {
        }


    }



}

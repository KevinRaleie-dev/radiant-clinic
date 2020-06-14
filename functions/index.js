const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp();
const db = admin.firestore();
const sgMail = require('@sendgrid/mail');

//import * as sgMail from '@sendgrid/mail';


const API_KEY = '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';
const TEMP = 'd-18fe3080c02046d5a065cf97e8d7210f';
sgMail.setApiKey(API_KEY);


//Functions

//Send email to user after signing up
exports.welcomeEmail = functions.auth.user().onCreate(user => {

const msg = {
            to: user.email,
            from: 'kkevraleie@gmail.com',
            templateId: TEMP,
            dynamic_template_data: {
                subject: 'Welcome to the Radiant Clinic app!',
                name: user.displayName,
            },

};

    return sgMail.send(msg);

});

//Send email via HTTPs. can be called from frontend code
exports.genericEmail = functions.https.onCall(async (data, context) =>{

    if(!context.auth && !context.auth.token.email){
         throw new functions.https.HttpsError('failed-precondition', 'Must be logged in with email address ')

    }
    const msg = {
        to: context.auth.token.email,
        from: 'kkevraleie@gmail.com',
        templateId: TEMP,
        dynamic_template_data: {
                subject: data.subject,
                name: data.text,
        },

    };

    await sgMail.send(msg);
    return { success: true };


});





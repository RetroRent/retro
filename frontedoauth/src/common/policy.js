import React, { Component } from 'react';
import './policy.css';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';

class Policy extends Component {
    render() {
        return (
            <div className="home-container">
                <Paper>
                    <Typography component="p">
                        <h4>תנאי שימוש </h4>
                    </Typography>
                    <Typography className="policyTitle">
                        ביטולי עיסקאות :
                    </Typography>
                    <Typography className="policyText">
                        1. המשתמש רשאי לבטל עיסקה שביצע באתר ע"פ חוק הגנת הצרכן, התשמ"א 1981 (להלן: "החוק הגנת הצרכן") ובהתאם למדיניות החזרת מוצרים שתפורסם מעת לעת לשיקול דעת החברה.
                    </Typography>

                    <Typography className="policyText">
                        2. ביטול הזמנה ייעשה מיום ביצוע ההזמנה עד ארבע עשר ימים מיום קבלת המוצר או קבלת מסמך ערוך לפי ס' 14ג(ב) בהתאם לחוק הגנת הצרכן.
                    </Typography>

                    <Typography className="policyText">
                        3. ביטול הזמנה לפי הכללים הנ"ל יחויב הלקוח בדמי ביטול של 5% או 100 ש"ח לפי הנמוך מבינהם ועל פי הדין.
                    </Typography>

                    <Typography className="policyText">
                        4. התנאים יחולו על המוצרים ע"פ חוק הגנת הצרכן התשמ"א 1981.
                    </Typography>

                    <Typography className="policyText">
                        5. ניתן לבטל הזמנה ללא חיוב תשלום עד 7 ימים מיום מועד ההשכרה\הזמנת השירות.
                    </Typography>
                    <Typography className="policyText">
                        6. ביטולים שייעשו בתוך 7 הימים למועד הזמנת שירותי השכרת ציוד יחויבו בסכום מלא.
                    </Typography>
                    <Typography className="policyTitle">
                        ביטול עיסקה יזומה ע"י החברה :
                    </Typography>
                    <Typography className="policyText">
                        7. הודעת "אישור הזמנה" אינו מהווה ראייה על ביצוע הפעולה, ואינו מחייב את החברה \ האתר. רק רישום שנרשם במחשבי החברה יהווה ראיה חלוטה לנכונות הפעולות.
                    </Typography>
                    <Typography className="policyText">
                        8. במקרה בו השוכר לא יוכל לספק את המוצר, השוכר יוכל להציע מוצר חלופי ו/או דומה ו/או תואם החזר כספי. החברה לא תישא בנזקים של המזמין ככל שייטען כן כאמור. כמו"כ תקום לחברה הזכות לבטל את העסקה.
                    </Typography>
                    <Typography className="policyText">
                        9. במידה ופרטי המזמין לא נקלטו כראוי מכל סיבה שהיא רשאית החברה לבטל את עיסקת הרכישה ע"פ שיקול דעתה.
                    </Typography>
                    <Typography className="policyText">
                        10. כאשר ההזמנה בוצעה שלא כדין ו/או לא על פי תקנון זה, החברה תהיה רשאית לבטל את העיסקה.
                    </Typography>
                    <Typography className="policyText">
                        11. הודעה על ביטול הזמנה תימסר ללקוח ע"פ הפרטים שמילא.
                    </Typography>
                    <Typography className="policyText">
                        12. באם נפלה טעות סופר כלשהי בהצעה ו/או בהזמנה, תהיה רשאית החברה לבטל את העיסקה.
                    </Typography>
                </Paper>
            </div>
        )
    }
}

export default Policy;
package com.kindred.mir.scene.login;

import com.kindred.mir.controls.*;
import com.kindred.mir.libs.MirImage;

public class NewAccountDialog extends MirControlWithStaticImage {

    public MirButton OKButton, CancelButton;

    public MirTextBox AccountIDTextBox,
            Password1TextBox,
            Password2TextBox,
            EMailTextBox,
            UserNameTextBox,
            BirthDateTextBox,
            QuestionTextBox,
            AnswerTextBox;

    public MirLabel Description;

    private boolean accountIDValid,
            password1Valid,
            password2Valid,
            eMailValid = true,
            userNameValid = true,
            birthDateValid = true,
            questionValid = true,
            answerValid = true;

    public NewAccountDialog(MirControl parent, long renderer_id, MirImage image) throws Exception
    {
        super(parent,renderer_id, image);
//        Index = 63;
//        Size = new Size();
//        setLocation(new Point((Settings.ScreenWidth - image.getWidth()) / 2, (Settings.ScreenHeight - image.getHeight()) / 2););
//
//        CancelButton = new MirButton
//        {
//            HoverIndex = 204,
//                    Index = 203,
//                    Library = Libraries.Title,
//                    Location = new Point(409, 425),
//                    Parent = this,
//                    PressedIndex = 205
//        };
//        CancelButton.Click += (o, e) => Dispose();
//
//        OKButton = new MirButton
//        {
//            Enabled = false,
//                    HoverIndex = 201,
//                    Index = 200,
//                    Library = Libraries.Title,
//                    Location = new Point(135, 425),
//                    Parent = this,
//                    PressedIndex = 202,
//        };
//        OKButton.Click += (o, e) => CreateAccount();
//
//
//        AccountIDTextBox = new MirTextBox
//        {
//            Border = true,
//                    BorderColour = Color.Gray,
//                    Location = new Point(226, 103),
//                    MaxLength = Globals.MaxAccountIDLength,
//                    Parent = this,
//                    Size = new Size(136, 18),
//        };
//        AccountIDTextBox.SetFocus();
//        AccountIDTextBox.TextBox.MaxLength = Globals.MaxAccountIDLength;
//        AccountIDTextBox.TextBox.TextChanged += AccountIDTextBox_TextChanged;
//        AccountIDTextBox.TextBox.GotFocus += AccountIDTextBox_GotFocus;
//
//        Password1TextBox = new MirTextBox
//        {
//            Border = true,
//                    BorderColour = Color.Gray,
//                    Location = new Point(226, 129),
//                    MaxLength = Globals.MaxPasswordLength,
//                    Parent = this,
//                    Password = true,
//                    Size = new Size(136, 18),
//                    TextBox = { MaxLength = Globals.MaxPasswordLength },
//        };
//        Password1TextBox.TextBox.TextChanged += Password1TextBox_TextChanged;
//        Password1TextBox.TextBox.GotFocus += PasswordTextBox_GotFocus;
//
//        Password2TextBox = new MirTextBox
//        {
//            Border = true,
//                    BorderColour = Color.Gray,
//                    Location = new Point(226, 155),
//                    MaxLength = Globals.MaxPasswordLength,
//                    Parent = this,
//                    Password = true,
//                    Size = new Size(136, 18),
//                    TextBox = { MaxLength = Globals.MaxPasswordLength },
//        };
//        Password2TextBox.TextBox.TextChanged += Password2TextBox_TextChanged;
//        Password2TextBox.TextBox.GotFocus += PasswordTextBox_GotFocus;
//
//        UserNameTextBox = new MirTextBox
//        {
//            Border = true,
//                    BorderColour = Color.Gray,
//                    Location = new Point(226, 189),
//                    MaxLength = 20,
//                    Parent = this,
//                    Size = new Size(136, 18),
//                    TextBox = { MaxLength = 20 },
//        };
//        UserNameTextBox.TextBox.TextChanged += UserNameTextBox_TextChanged;
//        UserNameTextBox.TextBox.GotFocus += UserNameTextBox_GotFocus;
//
//
//        BirthDateTextBox = new MirTextBox
//        {
//            Border = true,
//                    BorderColour = Color.Gray,
//                    Location = new Point(226, 215),
//                    MaxLength = 10,
//                    Parent = this,
//                    Size = new Size(136, 18),
//                    TextBox = { MaxLength = 10 },
//        };
//        BirthDateTextBox.TextBox.TextChanged += BirthDateTextBox_TextChanged;
//        BirthDateTextBox.TextBox.GotFocus += BirthDateTextBox_GotFocus;
//
//        QuestionTextBox = new MirTextBox
//        {
//            Border = true,
//                    BorderColour = Color.Gray,
//                    Location = new Point(226, 250),
//                    MaxLength = 30,
//                    Parent = this,
//                    Size = new Size(190, 18),
//                    TextBox = { MaxLength = 30 },
//        };
//        QuestionTextBox.TextBox.TextChanged += QuestionTextBox_TextChanged;
//        QuestionTextBox.TextBox.GotFocus += QuestionTextBox_GotFocus;
//
//        AnswerTextBox = new MirTextBox
//        {
//            Border = true,
//                    BorderColour = Color.Gray,
//                    Location = new Point(226, 276),
//                    MaxLength = 30,
//                    Parent = this,
//                    Size = new Size(190, 18),
//                    TextBox = { MaxLength = 30 },
//        };
//        AnswerTextBox.TextBox.TextChanged += AnswerTextBox_TextChanged;
//        AnswerTextBox.TextBox.GotFocus += AnswerTextBox_GotFocus;
//
//        EMailTextBox = new MirTextBox
//        {
//            Border = true,
//                    BorderColour = Color.Gray,
//                    Location = new Point(226, 311),
//                    MaxLength = 50,
//                    Parent = this,
//                    Size = new Size(136, 18),
//                    TextBox = { MaxLength = 50 },
//        };
//        EMailTextBox.TextBox.TextChanged += EMailTextBox_TextChanged;
//        EMailTextBox.TextBox.GotFocus += EMailTextBox_GotFocus;
//
//
//        Description = new MirLabel
//        {
//            Border = true,
//                    BorderColour = Color.Gray,
//                    Location = new Point(15, 340),
//                    Parent = this,
//                    Size = new Size(300, 70),
//                    Visible = false
//        };
    }
}

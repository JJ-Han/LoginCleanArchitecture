package app;

import interface_adapter.LoginViewModel;
import interface_adapter.SignupViewModel;
import interface_adapter.ViewManagerModel;
import view.LoginView;
import view.SignupView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Build the main program window, the main panel containing the
        // various cards, and the layout, and stitch them together.

        // The main application window.
        JFrame application = new JFrame("Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        CardLayout cardLayout = new CardLayout();

        // The various View objects. Only one view is visible at a time.
        JPanel views = new JPanel(cardLayout);
        application.add(views);

        // This keeps track of and manages which view is currently showing.
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        new ViewManager(views, cardLayout, viewManagerModel);

        // The data for the views, such as username and password, are in the ViewModels.
        // This information will be changed by a presenter object that is reporting the
        // results from the use case. The ViewModels are observable, and will
        // be observed by the Views.
        LoginViewModel loginViewModel = new LoginViewModel();
        SignupViewModel signupViewModel = new SignupViewModel();

        SignupView signupView = SignupUseCaseFactory.create(viewManagerModel, loginViewModel, signupViewModel);
        views.add(signupView, signupView.viewName);

        LoginView loginView = new LoginView(loginViewModel);
        views.add(loginView, loginView.viewName);

        viewManagerModel.setActiveView(signupView.viewName);
        viewManagerModel.firePropertyChanged();

        application.pack();
        application.setVisible(true);
    }
}

/*
Overview

Now that we've started to explore an example of the CA Engine in lecture, your team is ready to dig into the design and the code. Completing this activity will help prepare you for the coding activity, as well as your group project implementation for the rest of the term.
Step 1: Fork, clone, and run the application

Here is the repository: https://github.com/paulgries/LoginCleanArchitecture

Links to an external site. (same as from lecture last week)

Make sure everyone on the team can run app/Main before proceeding.
Step 2: Divide yourselves into pairs or threes

Do not work in a group of four or more. You almost always learn less


Step 3: View vs. View Model

Right now, the SignupView's actionPerformed method gets the username and two passwords from the various Java Swing text fields:

image.png
------------------------------------------------------------------------------
These three pieces of data also exist in the SignupViewModel.

    Question 1: what is the name of the instance variable in SignupView whose type is SignupViewModel?
        signupViewModel
    When does that variable get initialized?
        SignupViewModel signupViewModel = new SignupViewModel(); (in Main.java)
    What class instantiates SignupView?
        SignupView signupView = SignupUseCaseFactory.create(viewManagerModel, loginViewModel, signupViewModel); (in Main.java)
        SignupUseCaseFactory.create returns a signupView instance.

        Hint: IntelliJ will do a ton of work for you. On Mac, cmd-click on a name and see what happens. On Windows, use control-click. What tricks can IntelliJ do if you right-click on a name?

Learning goal: start to poke around in the code.

    Question 2: find "usernameInputField.addKeyListener(" and edit how that part of the code works.


Learning goal: understand how a ViewModel's state can be updated every keystroke.

This code is complex: first, the "new KeyListener() { … }" code creates an anonymous subclass of KeyListener and instantiates it, and that new object is passed to the addKeyListener method for the usernameInputField (where the user types their username). Method keyTyped is called every time the user types a letter, and keyTyped updates the SignupState object used by the SignupViewModel.

Yowza! You're going to have fun doing something similar for the password fields.

Write some code:

            Add anonymous listeners for passwordInputField and repeatPasswordInputField and update the SignupViewModel in the same way as the previous question.
            Make sure your program still compiles and runs.

Question 3: find the signUp.addActionListener( ... method call and change it

Learning goal: understand how a ViewModel's state can be accessed by the View.

The "new ActionListener" code creates an anonymous subclass of ActionListener and instantiates it, and then the addActionListener call adds that object as a listener to the signUp button.

Write some code:

            Rewrite the code inside the if statement to retrieve the username and two password attempts from the SignupViewModel's SignupState. Use the variable from Question 1. Let IntelliJ do most of the work creating that code!
            Make sure that your program still compiles and runs.

Step 4: The Data Access Interface

Question 1: Where do the interfaces belong?

Learning goal: start to think from the point of view of a use case interactor.

The interactors are the heart of the CA Engine. The SignupInteractor uses the following interfaces:

            How to invoke it (the SignupInputBoundary)
            The persistence services it requires (the UserSignupDataAccessInterface)
            How the interactor will present the information when complete (the SignupOutputBoundary)

In the case study, one of these interfaces is in the wrong package. Figure out which one (compare to the CA Engine picture), then drag and drop that interface into the correct package — IntelliJ will help automatically refactor any names as needed.

Question 2: how does the SignupInteractor get its DAO and Presenter?

Learning goal: think about how a use case interactor factory works.

Somewhere in the program, the SignupInteractor object is created (using new SignupInteractor(…)). In what order are the various CA classes instantiated?

Debug some code:

            Set a breakpoint on the first line of the SignupUseCaseFactory.create method body. Debug the program.
            As you go, make a list of the objects that are created: anything with "new". The first one, just inside the try block, is "new SignupViewModel", so write that down. Any time you're on a line that involves "new", write down the name of the class you are instantiating.
            When you get to the call on helper method createUserSignupUseCase, use Step Into to enter that method.
            What's the last object that gets created before method SignupView.create returns?

If you get to here, you are done the activity! We encourage you to continue to explore the code and start thinking about how you would go about implementing other use cases in the program. Your team should also continue to work on your project blueprint with any remaining time.



 */
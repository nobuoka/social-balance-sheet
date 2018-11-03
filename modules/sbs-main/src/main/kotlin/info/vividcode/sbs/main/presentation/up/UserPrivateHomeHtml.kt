package info.vividcode.sbs.main.presentation.up

import info.vividcode.sbs.main.core.domain.AccountBook
import info.vividcode.sbs.main.core.domain.User
import kotlinx.html.*

internal fun userPrivateHomeHtml(
        actor: User,
        userAccountBooks: List<AccountBook>,
        logoutPath: String,
        userAccountBooksPath: String,
        userPrivateAccountBookPathGenerator: (accountBook: AccountBook) -> String
): suspend TagConsumer<*>.() -> Unit = {
    html {
        head {
            meta(name = "viewport", content = "width=device-width,initial-scale=1")
            title("Social B/S")
            styleLink("/static/css/main.css")
        }
        body {
            h1 { +"Social B/S" }

            div {
                span { +"Hi, ${actor.displayName}!" }
            }
            form(method = FormMethod.post, action = logoutPath) {
                submitInput {
                    value = "Sign out"
                }
            }

            div {
                h2 { +"Your account books"}
                h3 { +"Add account book" }
                form(method = FormMethod.post, action = userAccountBooksPath) {
                    textInput(name = "label")
                    submitInput {
                        value = "Create account book"
                    }
                }
                h3 { +"Current account books" }
                if (userAccountBooks.isEmpty()) {
                    span { +"Not added yet." }
                } else {
                    ul {
                        userAccountBooks.forEach { accountBook ->
                            li {
                                a(href = userPrivateAccountBookPathGenerator(accountBook)) {
                                    +accountBook.label
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

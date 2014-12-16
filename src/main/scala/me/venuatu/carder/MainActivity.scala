package me.venuatu.carder

import android.app.Activity
import android.content.Intent
import android.graphics.{Point, Paint}
import android.net.Uri
import android.os.{Build, Bundle}
import android.text.util.Linkify
import android.view.{Gravity, View}
import android.view.View.OnClickListener
import android.widget.{ScrollView, ImageView, LinearLayout, TextView}
import android.support.v7.appcompat.R.style
import android.view.ViewGroup.LayoutParams._
import macroid.contrib.BgTweaks

// import macroid stuff
import macroid.FullDsl._
import macroid._

class MainActivity extends Activity with Contexts[Activity] {

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)

    val display = getWindowManager.getDefaultDisplay
    val minDimension =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        val size = new Point()
        display.getSize(size)
        Math.min(size.x, size.y)
      } else {
        Math.min(display.getWidth, display.getHeight)
      }

    val view = l[ScrollView](
      l[LinearLayout](
        w[ImageView] <~ BgTweaks.res(R.drawable.avatar)
          <~ lp[LinearLayout](minDimension, minDimension)
          <~ padding(all = 0)
        ,
        w[TextView] <~ text(R.string.name)
          <~ textStyle(style.TextAppearance_AppCompat_Headline)
          <~ textSize(16.dp)
        ,

        w[TextView] <~ text(R.string.title)
          <~ textStyle(style.TextAppearance_AppCompat_Subhead)
          <~ textSize(14.dp)
        ,

        w[TextView] <~ text(R.string.phone)
          <~ textStyle(style.TextAppearance_AppCompat_Large)
          <~ textSize(10.dp)
          <~ link("phone")
        ,

        w[TextView] <~ text(R.string.email)
          <~ textStyle(style.TextAppearance_AppCompat_Large)
          <~ textSize(10.dp)
          <~ link("email")
        ,

        w[TextView] <~ text(R.string.resume)
          <~ textStyle(style.TextAppearance_AppCompat_Large)
          <~ textSize(10.dp)
          <~ link("web")
        ,

        w[TextView] <~ text(R.string.twitter)
          <~ textStyle(style.TextAppearance_AppCompat_Large)
          <~ textSize(10.dp)
          <~ link("twitter")
        ,

        w[TextView] <~ text(R.string.github)
          <~ textStyle(style.TextAppearance_AppCompat_Large)
          <~ textSize(10.dp)
          <~ link("github")
        ,

        w[TextView] <~ text(R.string.linkedin)// <~ autoLink(Linkify.PHONE_NUMBERS)
          <~ textStyle(style.TextAppearance_AppCompat_Large)
          <~ textSize(10.dp)
          <~ link("linkedin")


//        ) <~ vertical
//          <~ padding(all = 4.dp)
//          <~ lp[LinearLayout](MATCH_PARENT, MATCH_PARENT)

      ) <~ lp[LinearLayout](MATCH_PARENT, MATCH_PARENT)
        <~ vertical
        <~ padding(bottom = 8.dp)

    ) <~ //(portrait ? vertical | horizontal) <~
     Transformer {
        case x: TextView => x <~
          padding(all = 4.dp) <~
          lp[LinearLayout](MATCH_PARENT, WRAP_CONTENT) <~
          textGravity(Gravity.CENTER)
      }

    setContentView(getUi(view))
  }

  def autoLink(linkType: Int) =
    Tweak[TextView]{_.setAutoLinkMask(linkType)}

  def link(linkType: String) =
    Tweak[TextView]{ textview =>
      // http://www.google.com/design/spec/style/color.html#color-color-palette teal 200
      textview.setTextColor(0xFF80CBC4)
      textview.setPaintFlags(textview.getPaintFlags | Paint.UNDERLINE_TEXT_FLAG)

      textview.setOnClickListener(new OnClickListener {
        override def onClick(view: View) {
          val text = textview.getText.toString
          linkType match {

            case "phone" =>
              val intent = new Intent(Intent.ACTION_DIAL)
              intent.setData(Uri.parse("tel:" + text))
              startActivity(intent)

            case "email" =>
              val intent = new Intent(Intent.ACTION_SENDTO)
              intent.setType("message/rfc822")
              intent.putExtra(Intent.EXTRA_EMAIL, text)
              intent.setData(Uri.parse("mailto:" + text))
              intent.putExtra(Intent.EXTRA_SUBJECT, "Card app")
              intent.putExtra(Intent.EXTRA_TEXT,
                "Hi!\n\nI downloaded your app and would like to get in contact with you!\n\nRegards."
              )
              startActivity(intent)

            case "twitter" =>
              val intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://twitter.com/" + text.replace("@", "")))
              startActivity(intent)

            case "web" | _ =>
              val intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + text))
              startActivity(intent)
          }
        }
      })
    }


  def textStyle(res: Int)(implicit ctx: ActivityContext) =
    Tweak[TextView]{_.setTextAppearance(ctx.get, res)}

  def textSize(size: Float) =
    Tweak[TextView]{_.setTextSize(size)}

  def textGravity(gravity: Int) =
    Tweak[TextView]{_.setGravity(gravity)}

}

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.houstd.R

class ReviewsFragment : Fragment(R.layout.activity_main2) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val container = view.findViewById<LinearLayout>(R.id.reviewsContainer)

        // Example data (replace with your API list)
        val reviews = listOf(
            ReviewUi("Ahmed Ali", "Faculty Of Engineering", 5f, "16-02-2021",
                "Highly Recommended, Every Thing Was So Nice", "1 Bed | Stayed 2 Months"),
            ReviewUi("Ahmed Ali", "Faculty Of Engineering", 5f, "16-02-2021",
                "Highly Recommended, Every Thing Was So Nice", "1 Bed | Stayed 2 Months"),
            ReviewUi("Ahmed Ali", "Faculty Of Engineering", 5f, "16-02-2021",
                "Highly Recommended, Every Thing Was So Nice", "1 Bed | Stayed 2 Months"),
        )

        // Update count text
        view.findViewById<TextView>(R.id.tvReviewsCount).text = "Reviews (${reviews.size})"

        // Add a fragment for each review
        reviews.forEach { r ->
            val host = FragmentContainerView(requireContext()).apply {
                id = View.generateViewId()
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            container.addView(host)

            childFragmentManager.beginTransaction()
                .replace(
                    host.id,
                    ReviewItemFragment.newInstance(
                        r.name, r.faculty, r.rating, r.date, r.comment, r.meta
                    )
                )
                .commit()
        }
    }
}

data class ReviewUi(
    val name: String,
    val faculty: String,
    val rating: Float,
    val date: String,
    val comment: String,
    val meta: String
)
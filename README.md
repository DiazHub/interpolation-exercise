#Task
* A curve was precomputed and 40% of the points were deleted.
* Implement a function that interpolates the missing points.

Initial thoughts:

    What's interpolation again?
        * Boils down to creating data points using pre existing ones
        * Several ways to implement
        * Some better than others, some more expensive than others, some more complicated
            * Nearest Neighbors  I took a data mining course where we studied this algorithm, let's try that
    Code Runthrough
        - Our input is a list of Point objects with some null
            This gave me problems at first because I was trying math functions on nulls, the Stack traces weren't pretty
        - Graphics library to print out points in different colors

Implementation Time:

    Spent some time troubleshooting the build (turns out I had an incompatible JRE)

    Logic:
        Create a list of Point objects to return to main
        Iterate through given list of Point objects
            Select a random point 
                -measure distance between that point and all of the points in the list
                -keep track of the point with minimum distance ( K = 1 , for KNN )
                -Once min dist is found, interpolate a point between the random point and the closest point to it 
                -add interpolated point to list of Point objects to return
            

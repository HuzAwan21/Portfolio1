# Your Portfolio Website

A modern, interactive, and fully customizable portfolio website. Perfect for showcasing your projects, research, presentations, and professional journey.

## 🌟 Features

- **Fully Responsive Design** - Works perfectly on desktop, tablet, and mobile devices
- **Interactive Elements** - Smooth animations, modals, and dynamic content
- **Easy to Customize** - Edit `data.js` to add your projects, research, CV, and contact info
- **Extendable** - Well-structured code makes it easy to add new sections
- **Modern UI** - Beautiful gradient designs, smooth transitions, and professional styling
- **Mobile-Friendly Navigation** - Hamburger menu for smaller screens
- **Project Filtering** - Filter projects by category
- **Social Links** - Connect your social media profiles
- **Contact Form** - Integrated email contact functionality
- **Gallery Support** - Showcase your photos and work
- **Research & Presentations** - Dedicated sections for academic content
- **CV Section** - Timeline view of experience and education

## 📁 File Structure

```
Portfolio_Huz/
├── index.html          # Main HTML file
├── styles.css          # All styling and animations
├── script.js           # Interactive functionality
├── data.js             # Your portfolio data (EDIT THIS FILE)
├── assets/
│   ├── images/         # Store your photos here
│   ├── documents/      # Store your CV and documents
│   └── projects/       # Store project files
└── README.md          # This file
```

## 🚀 Quick Start

1. **Open in Browser**: Simply open `index.html` in your web browser
2. **Edit Your Data**: Open `data.js` and fill in your information
3. **Add Profile Picture**: Add your photo URL in the `profileImage` field
4. **Add Projects**: Add your projects to the `projects` array
5. **Customize Colors**: Edit CSS variables in `styles.css` (`:root` section)

## 📝 How to Customize

### 1. Basic Information
In `data.js`, update these fields:
```javascript
const portfolioData = {
    name: "Your Name",
    title: "Your Title",
    bio: "Your bio here...",
    profileImage: "URL_TO_YOUR_PHOTO",
    cvUrl: "URL_TO_YOUR_CV"
};
```

### 2. Add Skills
```javascript
skills: [
    "Skill 1",
    "Skill 2",
    "Skill 3"
]
```

### 3. Add Projects
```javascript
projects: [
    {
        id: 1,
        title: "Project Name",
        description: "Brief description",
        category: "web",  // web, mobile, ai, other
        image: "🚀",
        tags: ["JavaScript", "React"],
        link: "https://github.com/yourproject",
        details: "Full project description"
    }
]
```

### 4. Add Research
```javascript
research: [
    {
        id: 1,
        title: "Paper Title",
        date: "2024",
        description: "Description",
        url: "https://link-to-paper.com",
        abstractShort: "Short abstract"
    }
]
```

### 5. Add Presentations
```javascript
presentations: [
    {
        id: 1,
        title: "Presentation Title",
        date: "January 2024",
        event: "Event Name",
        description: "Description",
        url: "https://presentation-link.com",
        icon: "📊"
    }
]
```

### 6. Add Experience
```javascript
experience: [
    {
        id: 1,
        position: "Job Title",
        company: "Company Name",
        duration: "2023 - Present",
        description: "Description",
        highlights: ["Achievement 1", "Achievement 2"]
    }
]
```

### 7. Add Education
```javascript
education: [
    {
        id: 1,
        degree: "Bachelor of Science",
        field: "Computer Science",
        institution: "University Name",
        year: "2023",
        details: "Relevant coursework"
    }
]
```

### 8. Contact Information
```javascript
contact: {
    email: "your.email@example.com",
    phone: "+1 (555) 123-4567",
    location: "City, Country",
    linkedIn: "https://linkedin.com/in/yourprofile",
    github: "https://github.com/yourprofile",
    twitter: "https://twitter.com/yourprofile",
    portfolio: "https://yourportfolio.com"
}
```

### 9. Social Media Links
```javascript
social: [
    { name: "LinkedIn", url: "https://linkedin.com", icon: "fab fa-linkedin" },
    { name: "GitHub", url: "https://github.com", icon: "fab fa-github" },
    // Add more...
]
```

## 🎨 Customizing Colors

Edit the CSS variables in `styles.css`:

```css
:root {
    --primary-color: #6366f1;      /* Main accent color */
    --secondary-color: #8b5cf6;    /* Secondary color */
    --accent-color: #ec4899;       /* Accent highlights */
    --dark-bg: #0f172a;            /* Dark background */
    --light-bg: #f8fafc;           /* Light background */
    --text-dark: #1e293b;          /* Dark text */
    --text-light: #64748b;         /* Light text */
}
```

## 📱 Sections Explained

- **Hero**: Welcome section with your name and title
- **About**: Your bio and skills
- **Projects**: Showcase your work with filtering
- **Research**: Academic papers and research
- **Presentations**: Talks and presentations
- **Gallery**: Photo showcase
- **CV**: Experience and education timeline
- **Contact**: Contact form and information

## 🔗 Adding External Links

Use these emoji icons for projects:
- 🚀 Web projects
- 📱 Mobile apps
- 🤖 AI/ML projects
- 🎨 Design projects
- 📊 Data projects
- And many more!

## 💡 Tips

1. **Images**: Host images on services like Imgur, Cloudinary, or GitHub
2. **CV PDF**: Store your CV in Google Drive and get the shareable link
3. **Project Links**: Link to GitHub, live demos, or personal project pages
4. **Emojis**: Use Unicode Emoji Characters for visual appeal
5. **Responsiveness**: The site automatically adapts to all screen sizes

## 🎯 Deployment

### Option 1: GitHub Pages (Free)
1. Create a GitHub repository
2. Push your files
3. Enable GitHub Pages in repository settings
4. Your site will be live at `username.github.io/repository-name`

### Option 2: Netlify (Free)
1. Drag and drop your folder into Netlify
2. Get a live URL instantly

### Option 3: Vercel (Free)
1. Connect your GitHub repository
2. Deploy with one click

### Option 4: Traditional Hosting
1. Purchase hosting
2. Upload files via FTP
3. Your site goes live

## 🛠️ Technologies Used

- **HTML5**: Semantic markup
- **CSS3**: Modern styling with animations and gradients
- **JavaScript**: Vanilla JS for interactivity
- **Font Awesome**: Icons
- **Google Fonts**: Typography (optional)

## 📞 Contact Integration

The contact form opens your default email client. To integrate with email services:

1. Remove the contact form HTML
2. Use services like FormSubmit, Formspree, or Firebase
3. Add the service endpoint to the form

## ✨ Future Enhancements

Consider adding:
- Dark mode toggle
- Multi-language support
- Blog section
- Comment section
- Analytics integration
- Email notification system

## 📄 License

This portfolio template is free to use and modify for personal use.

## 🎉 Final Notes

- Always update `data.js` with fresh information
- Keep your portfolio link ready for job applications
- Update projects and research regularly
- Share on social media and professional networks
- Get feedback from peers and improve

**Happy Sharing! 🚀**
